CREATE TABLE tipos (
    id_tipo INT         NOT NULL AUTO_INCREMENT,
    tipo    VARCHAR(10) NOT NULL,

    CONSTRAINT PK_tipos           PRIMARY KEY (id_tipo),
    CONSTRAINT FK_tipos_tipoavion FOREIGN KEY (tipo) REFERENCES tipos_avion(nombre)
);

INSERT INTO tipos (tipo) VALUES
('PASAJERO'),
('CARGA');