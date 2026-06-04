package com.fullstack.SoftwareAviones.msvuelo.DTO;
import java.util.Date;

import lombok.Data;

@Data
public class VueloDTO {
    private Integer ID_vuelo;
    private String numero_vuelo;
    private Date hora_inicio_vuelo;
    private String tipo_vuelo;
    private String destino;
    private Integer idPiloto;
    private Integer idAvion;
    private Integer idAerodromo;
    private String nombrePiloto;      // viene de ms-piloto
    private String modeloAvion;       // viene de ms-avion
    private String nombreAerodromo;   // viene de ms-ubicacion
}
