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
@Table(name = "fabricantes")

public class Fabricante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_fabricante;

    @NotBlank(message = "El nombre del fabricante es obligatorio")
    @Size(min = 5, max = 15, message = "El nombre del fabricante debe tener de 5 a 15 caracteres")
    @Column(nullable = false, unique = true, length = 15)
    private String nombre_fabricante;

    @OneToMany(mappedBy = "fabricante")
    private List<Avion> aviones;
    

}
