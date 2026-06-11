package com.fullstack.SoftwareAviones.msavion.DTO;

import lombok.Data;

import java.util.List;

@Data
public class TipoDTO{
    private Integer id_tipo;
    private String tipo;
    private List<String> aviones;
}
