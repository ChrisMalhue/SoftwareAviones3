CREATE TABLE aviones (
    ID_avion              INT          NOT NULL AUTO_INCREMENT,
    matricula             VARCHAR(8)   NOT NULL,
    modelo                VARCHAR(50)  NOT NULL,
    ID_tipo               INT          NOT NULL,
    capacidad_pasajero    INT          NULL,
    capacidad_carga_kg    DOUBLE       NULL,
    alcance_km            DOUBLE       NULL,
    cantidad_asientos_vip INT          NULL,
    envergadura_metros    DOUBLE       NOT NULL,
    capacidad_combustible DOUBLE       NOT NULL,
    ID_fabricante         INT          NOT NULL,
    ID_origen             INT          NOT NULL,

    CONSTRAINT PK_aviones           PRIMARY KEY (ID_avion),
    CONSTRAINT UQ_aviones_matricula UNIQUE (matricula),
    CONSTRAINT FK_aviones_tipo      FOREIGN KEY (ID_tipo)       REFERENCES tipos(id_tipo),
    CONSTRAINT FK_aviones_fab       FOREIGN KEY (ID_fabricante) REFERENCES fabricantes(id_fabricante),
    CONSTRAINT FK_aviones_origen    FOREIGN KEY (ID_origen)     REFERENCES origenes(id_origen),
    CONSTRAINT CHK_envergadura      CHECK (envergadura_metros    >= 0),
    CONSTRAINT CHK_combustible      CHECK (capacidad_combustible >= 0)
);

INSERT INTO aviones (matricula, modelo, ID_tipo, capacidad_pasajero, envergadura_metros, capacidad_combustible, ID_fabricante, ID_origen) VALUES
('CC-ABC', '737-800', 1, 160, 35.8, 26020.0, 1, 1);

INSERT INTO aviones (matricula, modelo, ID_tipo, capacidad_carga_kg, envergadura_metros, capacidad_combustible, ID_fabricante, ID_origen) VALUES
('CC-CGO', 'A300-600F', 2, 57600.0, 44.8, 62000.0, 2, 2);