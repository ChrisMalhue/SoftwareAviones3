package com.fullstack.SoftwareAviones.msavion.DTO;

import lombok.Data;

import java.util.List;

@Data
public class FabricanteDTO{
    private Integer id_fabricante;
    private String nombre_fabricante;
    private List<String> aviones;
}