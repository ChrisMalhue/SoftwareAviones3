package com.fullstack.SoftwareAviones.msavion.model;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "origenes")

public class Origen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_origen;

    @NotBlank(message = "El pais de origen es obligatorio")
    @Size(min = 3, max = 15, message = "pais de origen debe tener de 3 a 15 caracteres")
    @Column(nullable = false, unique = true, length = 15)
    private String pais_origen;

    @OneToMany(mappedBy = "origen")
    private List<Avion> aviones;

}

