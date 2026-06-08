CREATE TABLE vuelos (
    ID_vuelo           INT          NOT NULL AUTO_INCREMENT,
    numero_vuelo       VARCHAR(5)   NOT NULL UNIQUE,
    hora_inicio_vuelo  DATETIME     NOT NULL,
    tipo_vuelo         VARCHAR(30)  NOT NULL,
    destino            VARCHAR(50)  NOT NULL,
    ID_piloto          INT          NOT NULL,
    ID_avion           INT          NOT NULL,
    ID_aerodromo       INT          NOT NULL,
 
    CONSTRAINT PK_vuelos PRIMARY KEY (ID_vuelo)
);
 
-- Vuelo 1: Carlos González pilotea el Boeing 737-800 desde Aeródromo El Bosque
-- Vuelo 2: María Martínez usa el Airbus A300-600F desde Aeródromo Tobalaba

INSERT INTO vuelos (numero_vuelo, hora_inicio_vuelo, tipo_vuelo, destino, ID_piloto, ID_avion, ID_aerodromo) VALUES
('LA101', '2026-06-10 08:00:00', 'Vuelo Comercial', 'Santiago', 1, 1, 1),
('LA202', '2026-06-10 14:00:00', 'Vuelo de Carga',  'Valparaíso', 2, 2, 2);