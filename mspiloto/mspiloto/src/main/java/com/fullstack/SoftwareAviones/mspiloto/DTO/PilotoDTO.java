package com.fullstack.SoftwareAviones.mspiloto.DTO;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PilotoDTO {

    private Integer ID_piloto;
    private String rut;
    private String nombre;
    private String apellido;
    private Date fecha_nacimiento;
    private Integer horas_vuelo;
    private List<String> cursosAprendidos;

}
