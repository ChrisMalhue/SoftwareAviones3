package com.fullstack.SoftwareAviones.msubicacion.DTO;

import java.util.List;

import lombok.Data;

@Data
public class ComunaDTO {

    private Integer ID_comuna;
    private String comuna;
    private String region;
    private List<String> aerodromos;
    
}
