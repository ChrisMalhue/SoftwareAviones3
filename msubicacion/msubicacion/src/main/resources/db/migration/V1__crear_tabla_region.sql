CREATE TABLE regiones (
    ID_region INT NOT NULL AUTO_INCREMENT,
    region VARCHAR(100) NOT NULL,
    PRIMARY KEY (ID_region)
);

INSERT INTO regiones (region) VALUES
('Metropolitana de Santiago'),
('Valparaíso');