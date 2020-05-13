-- Create Database
DROP DATABASE IF EXISTS vocabularies;
CREATE DATABASE IF NOT EXISTS vocabularies;

-- Table Creation
CREATE TABLE IF NOT EXISTS language
(
 language_id    serial NOT NULL,
 language_short varchar(10) NOT NULL,
 language_long  varchar(50) NOT NULL,
 CONSTRAINT PK_language PRIMARY KEY ( language_id )
);


CREATE TABLE IF NOT EXISTS vocabulary
(
 vocabulary_id serial NOT NULL,
 language_id   integer NOT NULL,
 group_id    integer NOT NULL,
 word         varchar(50) NOT NULL,
 CONSTRAINT PK_vocabulary PRIMARY KEY ( vocabulary_id ),
 CONSTRAINT FK_67 FOREIGN KEY ( language_id ) REFERENCES language ( language_id )
);

-- Index creation

CREATE INDEX "fkIdx_67" ON vocabulary
(
 language_id
);

-- Inserting Languages

INSERT INTO language (language_short, language_long) VALUES ('en', 'English');
INSERT INTO language (language_short, language_long) VALUES ('es', 'Spanish');

-- Inserting Vocabularies

INSERT INTO vocabulary (language_id, group_id, word) VALUES (1, 1, 'Der Zug');
INSERT INTO vocabulary (language_id, group_id, word) VALUES (2, 1, 'El Tren');
INSERT INTO vocabulary (language_id, group_id, word) VALUES (1, 2, 'Das Flugzeug');
INSERT INTO vocabulary (language_id, group_id, word) VALUES (2, 2, 'El Avión');
INSERT INTO vocabulary (language_id, group_id, word) VALUES (1, 3, 'Das Auto');
INSERT INTO vocabulary (language_id, group_id, word) VALUES (2, 3, 'El Carro');


COMMIT;

ANALYZE;