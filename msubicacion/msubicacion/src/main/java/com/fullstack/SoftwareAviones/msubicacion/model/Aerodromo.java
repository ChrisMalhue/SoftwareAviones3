package com.fullstack.SoftwareAviones.msubicacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "aerodromos")
public class Aerodromo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID_aerodromo;

    @NotBlank (message = "El nombre del aerodromo es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre del aerodromo debe tener entre 5 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre_aerodromo;

    @ManyToOne
    @JoinColumn(name = "ID_comuna")
    private Comuna comuna;

    

}
