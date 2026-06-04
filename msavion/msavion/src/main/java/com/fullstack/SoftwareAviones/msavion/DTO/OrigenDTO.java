package com.fullstack.SoftwareAviones.msavion.DTO;

import java.util.List;

import lombok.Data;

@Data
public class OrigenDTO {

    private Integer id_origen;
    private String pais_origen;
    private List<String> aviones;

}
