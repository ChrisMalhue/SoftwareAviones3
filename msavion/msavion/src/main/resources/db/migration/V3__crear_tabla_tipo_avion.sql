CREATE TABLE tipos_avion (
    id_tipo_avion INT         NOT NULL AUTO_INCREMENT,
    nombre        VARCHAR(10) NOT NULL,

    CONSTRAINT PK_tipos_avion        PRIMARY KEY (id_tipo_avion),
    CONSTRAINT UQ_tipos_avion_nombre UNIQUE (nombre),
    CONSTRAINT CHK_tipos_avion       CHECK (nombre IN ('PRIVADO', 'CARGA', 'GUERRA', 'PASAJERO'))
);

INSERT INTO tipos_avion (nombre) VALUES
('PRIVADO'),
('CARGA'),
('GUERRA'),
('PASAJERO');