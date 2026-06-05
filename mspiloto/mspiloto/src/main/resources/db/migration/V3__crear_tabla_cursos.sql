CREATE TABLE cursos_piloto (
    ID_cursos  INT NOT NULL AUTO_INCREMENT,
    ID_piloto  INT NOT NULL,
    ID_curso   INT NOT NULL,

    CONSTRAINT PK_cursos_piloto  PRIMARY KEY (ID_cursos),
    CONSTRAINT FK_cursos_piloto  FOREIGN KEY (ID_piloto) REFERENCES pilotos(ID_piloto),
    CONSTRAINT FK_cursos_curso   FOREIGN KEY (ID_curso)  REFERENCES cursos(ID_curso)
);

INSERT INTO cursos_piloto (ID_piloto, ID_curso) VALUES
(1, 1),
(2, 2);