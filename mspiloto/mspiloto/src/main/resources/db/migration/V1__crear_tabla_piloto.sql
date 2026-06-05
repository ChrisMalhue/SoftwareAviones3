CREATE TABLE pilotos (
    ID_piloto        INT          NOT NULL AUTO_INCREMENT,
    rut              VARCHAR(12)  NOT NULL UNIQUE,
    nombre           VARCHAR(100) NOT NULL,
    apellido         VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE         NOT NULL,
    horas_vuelo      INT          NOT NULL,

    CONSTRAINT PK_pilotos PRIMARY KEY (ID_piloto),
    CONSTRAINT CHK_horas_vuelo CHECK (horas_vuelo >= 0)
);
    INSERT INTO pilotos (rut, nombre, apellido, fecha_nacimiento, horas_vuelo) VALUES
    ('12.345.678-9', 'Carlos', 'González', '1985-03-15', 1500),
    ('98.765.432-1', 'María',  'Martínez', '1990-07-22', 850);