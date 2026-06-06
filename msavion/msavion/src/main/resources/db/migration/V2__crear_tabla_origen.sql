CREATE TABLE origenes (
    id_origen   INT         NOT NULL AUTO_INCREMENT,
    pais_origen VARCHAR(15) NOT NULL,

    CONSTRAINT PK_origenes PRIMARY KEY (id_origen),
    CONSTRAINT UQ_origenes_pais UNIQUE (pais_origen)
);

INSERT INTO origenes (pais_origen) VALUES
('USA'),
('Francia');