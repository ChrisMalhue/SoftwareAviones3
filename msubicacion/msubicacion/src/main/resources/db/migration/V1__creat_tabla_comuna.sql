CREATE TABLE comunas (
    ID_comuna INT NOT NULL AUTO_INCREMENT,
    comuna VARCHAR(100) NOT NULL,
    ID_region INT,
    PRIMARY KEY (ID_comuna),
    FOREIGN KEY (ID_region) REFERENCES regiones(ID_region)
);

INSERT INTO comunas (comuna, ID_region) VALUES
('Santiago', 1),
('Maipú', 1);