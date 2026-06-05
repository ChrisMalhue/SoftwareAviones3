CREATE TABLE aerodromos (
    ID_aerodromo INT NOT NULL AUTO_INCREMENT,
    nombre_aerodromo VARCHAR(100) NOT NULL,
    ID_comuna INT,
    PRIMARY KEY (ID_aerodromo),
    FOREIGN KEY (ID_comuna) REFERENCES comunas(ID_comuna)
);

INSERT INTO aerodromos (nombre_aerodromo, ID_comuna) VALUES
('Aeródromo El Bosque', 1),
('Aeródromo Tobalaba', 2);



