package com.fullstack.SoftwareAviones.msavion.DTO;

import lombok.Data;

@Data
public class AvionDTO{
    private Integer ID_avion;
    private String matricula;
    private String marca;
    private String modelo;
    private String tipo;
    private Integer capacidad_pasajero;
    private Double capacidad_carga_kg;
    private Double alcance_km;
    private Integer cantidad_asientos_vip;
    private Double envergadura_metros;
    private Double capacidad_combustible;
    private String fabricante;
    private String origen;
}
