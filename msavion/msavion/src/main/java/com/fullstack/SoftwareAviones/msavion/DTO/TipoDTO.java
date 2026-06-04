package com.fullstack.SoftwareAviones.msavion.DTO;

import java.util.List;

import lombok.Data;

@Data
public class TipoDTO {

    private Integer id_tipo;
    private String tipo;
    private List<String> aviones;
    
}
