CREATE TABLE IF NOT EXISTS "MediaType"
(
    "id"   SERIAL PRIMARY KEY,
    "name" TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS "Movie"
(
    "id"           SERIAL PRIMARY KEY,
    "name"         TEXT NOT NULL,
    "release_date" DATE CHECK ("release_date" >= '1900-01-01') NOT NULL,
    "director"     TEXT NOT NULL,
    "company"      TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS "Exemplar"
(
    "id"         SERIAL PRIMARY KEY,
    "movie_id"   INTEGER NOT NULL,
    "media_type_id" INTEGER NOT NULL,
    "available"  BOOLEAN NOT NULL,
    CONSTRAINT "FK_Exemplar_movie_id"
        FOREIGN KEY ("movie_id")
            REFERENCES "Movie" ("id"),
    CONSTRAINT "FK_Exemplar_media_type"
        FOREIGN KEY ("media_type_id")
            REFERENCES "MediaType" ("id")
);

CREATE TABLE IF NOT EXISTS "Client"
(
    "id"           SERIAL PRIMARY KEY,
    "full_name"    TEXT NOT NULL,
    "address"      TEXT,
    "phone_number" TEXT NOT NULL,
    "image_path"   TEXT
);

CREATE TYPE transaction_type AS ENUM ('issue', 'return');

CREATE TABLE IF NOT EXISTS "Transaction"
(
    "id"          SERIAL PRIMARY KEY,
    "exemplar_id" INTEGER NOT NULL,
    "client_id"   INTEGER NOT NULL,
    "time"        TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "type"        transaction_type NOT NULL,
    CONSTRAINT "FK_Transaction_exemplar_id"
        FOREIGN KEY ("exemplar_id")
            REFERENCES "Exemplar" ("id"),
    CONSTRAINT "FK_Transaction_client_id"
        FOREIGN KEY ("client_id")
            REFERENCES "Client" ("id")
);

CREATE VIEW "IssuedExemplars" AS
SELECT C.id AS client_id, E.id AS exemplar_id
FROM "Transaction" T
         JOIN "Client" C on T.client_id = C.id
         JOIN "Exemplar" E on T.exemplar_id = E.id
WHERE E.available = FALSE
  AND T.type = 'issue'
  AND NOT EXISTS (SELECT 1
                  FROM "Transaction" T2
                  WHERE T2.exemplar_id = E.id
                    AND T2.type = 'return'
                    AND T2.time > T.time);
