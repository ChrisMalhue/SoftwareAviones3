package com.fullstack.SoftwareAviones.msvuelo.DTO;

import lombok.Data;

@Data
public class AvionesDTO {
    private Integer ID_aviones;
    private Integer idPiloto;
    private Integer idAvion;
    private String nombrePiloto;  // viene desde el ms-piloto
    private String modeloAvion;   // viene desde ms-avion
}