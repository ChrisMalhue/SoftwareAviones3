package com.fullstack.SoftwareAviones.mspiloto.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "cursos_piloto")
public class Cursos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID_cursos;

    @ManyToOne
    @JoinColumn(name = "ID_piloto")
    private Piloto piloto;

    @ManyToOne
    @JoinColumn(name = "ID_curso")
    private Curso curso;

}
