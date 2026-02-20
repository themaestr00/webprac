CREATE TABLE IF NOT EXISTS "MediaType"
(
    "id"   SERIAL PRIMARY KEY,
    "name" TEXT
);

CREATE TABLE IF NOT EXISTS "Movie"
(
    "id"           SERIAL PRIMARY KEY,
    "name"         TEXT,
    "release_date" DATE CHECK ("release_date" >= '1900-01-01'),
    "director"     TEXT,
    "company"      TEXT
);

CREATE TABLE IF NOT EXISTS "Exemplar"
(
    "id"         SERIAL PRIMARY KEY,
    "movie_id"   INTEGER,
    "media_type" INTEGER,
    "available"  BOOLEAN,
    CONSTRAINT "FK_Exemplar_movie_id"
        FOREIGN KEY ("movie_id")
            REFERENCES "Movie" ("id"),
    CONSTRAINT "FK_Exemplar_media_type"
        FOREIGN KEY ("media_type")
            REFERENCES "MediaType" ("id")
);

CREATE TABLE IF NOT EXISTS "Client"
(
    "id"           SERIAL PRIMARY KEY,
    "full_name"    TEXT,
    "address"      TEXT,
    "phone_number" TEXT,
    "image_path"   TEXT
);

CREATE TABLE IF NOT EXISTS "Transaction"
(
    "id"          SERIAL PRIMARY KEY,
    "exemplar_id" INTEGER,
    "client_id"   INTEGER,
    "time"        TIMESTAMPTZ,
    CONSTRAINT "FK_Transaction_exemplar_id"
        FOREIGN KEY ("exemplar_id")
            REFERENCES "Exemplar" ("id"),
    CONSTRAINT "FK_Transaction_client_id"
        FOREIGN KEY ("client_id")
            REFERENCES "Client" ("id")
);
