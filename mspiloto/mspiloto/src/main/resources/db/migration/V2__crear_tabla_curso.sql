CREATE TABLE cursos (
    ID_curso     INT          NOT NULL AUTO_INCREMENT,
    nombre_curso VARCHAR(100) NOT NULL,

    CONSTRAINT PK_cursos PRIMARY KEY (ID_curso)
);

INSERT INTO cursos (nombre_curso) VALUES
('Vuelo Instrumental'),
('Navegación Aérea Avanzada');