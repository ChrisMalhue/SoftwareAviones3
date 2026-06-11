package com.fullstack.SoftwareAviones.msavion.DTO;

import lombok.Data;

import java.util.List;

@Data
public class OrigenDTO{
    private Integer id_origen;
    private String pais_origen;
    private List<String> aviones;
}
