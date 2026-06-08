CREATE TABLE aviones_piloto (
    ID_aviones  INT NOT NULL AUTO_INCREMENT,
    ID_piloto   INT NOT NULL,
    ID_avion    INT NOT NULL,
 
    CONSTRAINT PK_avionesPiloto PRIMARY KEY (ID_aviones)
);
 
-- Piloto 1 (Carlos González) -> Avión 1 (Boeing 737-800 CC-ABC)
-- Piloto 2 (María Martínez)  -> Avión 2 (Airbus A300-600F CC-CGO)
INSERT INTO aviones_piloto (ID_piloto, ID_avion) VALUES
(1, 1),
(2, 2);