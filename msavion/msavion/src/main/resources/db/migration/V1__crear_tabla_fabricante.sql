CREATE TABLE fabricantes (
    id_fabricante      INT         NOT NULL AUTO_INCREMENT,
    nombre_fabricante  VARCHAR(15) NOT NULL,

    CONSTRAINT PK_fabricantes PRIMARY KEY (id_fabricante),
    CONSTRAINT UQ_fabricantes_nombre UNIQUE (nombre_fabricante)
);

INSERT INTO fabricantes (nombre_fabricante) VALUES
('Boeing'),
('Airbus');