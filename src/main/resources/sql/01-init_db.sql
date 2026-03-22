CREATE TABLE IF NOT EXISTS media_type
(
    "id"   BIGSERIAL PRIMARY KEY,
    "name" TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS movie
(
    "id"           BIGSERIAL PRIMARY KEY,
    "name"         TEXT                                        NOT NULL,
    "release_date" DATE CHECK ("release_date" >= '1900-01-01') NOT NULL,
    "director"     TEXT                                        NOT NULL,
    "company"      TEXT                                        NOT NULL,
    "is_deleted"   BOOLEAN                                     NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS exemplar
(
    "id"            BIGSERIAL PRIMARY KEY,
    "movie_id"      BIGINT  NOT NULL,
    "media_type_id" BIGINT  NOT NULL,
    "available"     BOOLEAN NOT NULL DEFAULT TRUE,
    "is_deleted"    BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT "FK_Exemplar_movie_id"
        FOREIGN KEY ("movie_id")
            REFERENCES movie ("id"),
    CONSTRAINT "FK_Exemplar_media_type"
        FOREIGN KEY ("media_type_id")
            REFERENCES media_type ("id")
);

CREATE TABLE IF NOT EXISTS client
(
    "id"           BIGSERIAL PRIMARY KEY,
    "full_name"    TEXT        NOT NULL,
    "address"      TEXT        NOT NULL,
    "phone_number" TEXT        NOT NULL,
    "image_path"   TEXT UNIQUE NOT NULL,
    "is_deleted"   BOOLEAN     NOT NULL DEFAULT FALSE,
    UNIQUE ("full_name", "phone_number")
);

DO
$$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_type WHERE typname = 'transaction_type') THEN
            CREATE TYPE transaction_type AS ENUM ('issue', 'returning');
        END IF;
    END
$$;

CREATE TABLE IF NOT EXISTS transaction
(
    "id"          BIGSERIAL PRIMARY KEY,
    "exemplar_id" BIGINT           NOT NULL,
    "client_id"   BIGINT           NOT NULL,
    "time"        TIMESTAMPTZ      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "type"        transaction_type NOT NULL,
    CONSTRAINT "FK_Transaction_exemplar_id"
        FOREIGN KEY ("exemplar_id")
            REFERENCES exemplar ("id"),
    CONSTRAINT "FK_Transaction_client_id"
        FOREIGN KEY ("client_id")
            REFERENCES client ("id")
);

CREATE INDEX IF NOT EXISTS idx_transaction_exemplar ON transaction (exemplar_id, time DESC);

CREATE OR REPLACE VIEW issued_exemplar AS
SELECT c.id AS client_id, e.id AS exemplar_id
FROM transaction t
         JOIN client c on t.client_id = c.id
         JOIN exemplar e on t.exemplar_id = e.id
WHERE e.available = FALSE
  AND t.type = 'issue'
  AND NOT EXISTS (SELECT 1
                  FROM transaction t2
                  WHERE t2.exemplar_id = e.id
                    AND t2.type = 'returning'
                    AND t2.time > t.time);

CREATE OR REPLACE FUNCTION process_transaction_and_update_exemplar()
    RETURNS TRIGGER AS
$$
DECLARE
    is_deleted_client   BOOLEAN;
    is_deleted_exemplar BOOLEAN;
    current_status      BOOLEAN;
BEGIN
    SELECT is_deleted
    INTO is_deleted_client
    FROM client
    WHERE id = NEW.client_id;

    IF is_deleted_client THEN
        RAISE EXCEPTION 'Ошибка: Попытка совершить транзакцию с удаленным пользователем с ID %s', NEW.client_id;
    END IF;

    SELECT is_deleted
    INTO is_deleted_exemplar
    FROM exemplar
    WHERE id = NEW.exemplar_id;

    IF is_deleted_exemplar THEN
        RAISE EXCEPTION 'Ошибка: Попытка совершить транзакцию с удаленным экземлпяром с ID %s', NEW.exemplar_id;
    END IF;

    SELECT available
    INTO current_status
    FROM exemplar
    WHERE id = NEW.exemplar_id
        FOR UPDATE;

    IF NEW.type = 'issue' THEN
        IF current_status = FALSE THEN
            RAISE EXCEPTION 'Ошибка: Экземпляр с ID % уже выдан!', NEW.exemplar_id;
        END IF;
        UPDATE exemplar SET available = FALSE WHERE id = NEW.exemplar_id;

    ELSIF NEW.type = 'returning' THEN
        IF current_status = TRUE THEN
            RAISE EXCEPTION 'Ошибка: Экземпляр с ID % числится в наличии, возврат невозможен!', NEW.exemplar_id;
        END IF;
        UPDATE exemplar SET available = TRUE WHERE id = NEW.exemplar_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_update_exemplar_availability ON transaction;

CREATE TRIGGER trg_update_exemplar_availability
    AFTER INSERT
    ON transaction
    FOR EACH ROW
EXECUTE FUNCTION process_transaction_and_update_exemplar();

CREATE OR REPLACE FUNCTION check_client_deactivation()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.is_deleted = FALSE AND OLD.is_deleted = TRUE THEN
        IF EXISTS (SELECT 1 FROM issued_exemplar WHERE client_id = OLD.id) THEN
            RAISE EXCEPTION 'Ошибка: Невозможно удалить клиента (ID %), у него есть невозвращенные экземпляры!', OLD.id;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_client_deactivation
    BEFORE UPDATE OF is_deleted
    ON client
    FOR EACH ROW
EXECUTE FUNCTION check_client_deactivation();

CREATE OR REPLACE FUNCTION check_exemplar_deactivation()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.is_deleted = FALSE AND OLD.is_deleted = TRUE THEN
        IF OLD.available = FALSE THEN
            RAISE EXCEPTION 'Ошибка: Невозможно списать экземпляр (ID %), так как он находится на руках у клиента!', OLD.id;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_exemplar_deactivation
    BEFORE UPDATE OF is_deleted
    ON exemplar
    FOR EACH ROW
EXECUTE FUNCTION check_exemplar_deactivation();

CREATE OR REPLACE FUNCTION check_movie_deactivation()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.is_deleted = FALSE AND OLD.is_deleted = TRUE THEN
        IF EXISTS (SELECT 1 FROM exemplar WHERE movie_id = OLD.id AND available = FALSE) THEN
            RAISE EXCEPTION 'Ошибка: Невозможно удалить фильм (ID %), так как один или несколько его экземпляров выданы клиентам!', OLD.id;
        END IF;

        UPDATE exemplar
        SET is_deleted = FALSE
        WHERE movie_id = OLD.id
          AND is_deleted = TRUE;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_movie_deactivation
    BEFORE UPDATE OF is_deleted
    ON movie
    FOR EACH ROW
EXECUTE FUNCTION check_movie_deactivation();
