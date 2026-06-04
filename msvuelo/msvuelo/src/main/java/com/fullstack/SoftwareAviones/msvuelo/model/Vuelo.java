package com.fullstack.SoftwareAviones.msvuelo.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vuelos")
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID_vuelo;

    @NotBlank(message = "El número de vuelo es obligatorio")
    @Size(min = 2, max = 5, message = "El número de vuelo debe tener de 2 a 5 digitos")
    @Column(nullable = false, unique = true, length = 5)
    private String numero_vuelo;

    @NotNull(message = "La hora de inicio del vuelo es obligatoria")
    @FutureOrPresent(message = "La hora de vuelo no puede ser pasada")
    @Column(nullable = false)
    private Date hora_inicio_vuelo;

    @NotBlank(message = "El tipo de vuelo es obligatorio")
    @Size(min = 10, max = 30, message = "El tipo de vuelo debe tener entre 10 y 30 caracteres")
    @Column(nullable = false, length = 30)
    private String tipo_vuelo;

    @NotBlank(message = "El destino es obligatorio")
    @Size(min = 5, max = 50, message = "El destino debe tener entre 5 a 50 caracteres")
    @Column(nullable = false, length = 50)
    private String destino;

    @NotNull(message = "El ID del piloto es obligatorio")
    @Column(name = "ID_piloto", nullable = false)
    private Integer idPiloto;

    @NotNull(message = "El ID del avion es obligatorio")
    @Column(name = "ID_avion", nullable = false)
    private Integer idAvion;

    @NotNull(message = "El ID del aerodromo es obligatorio")
    @Column(name = "ID_aerodromo", nullable = false)
    private Integer idAerodromo;
    
}

