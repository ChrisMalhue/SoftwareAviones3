package com.fullstack.SoftwareAviones.msavion.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FabricanteDTO extends RepresentationModel<FabricanteDTO> {
    private Integer id_fabricante;
    private String nombre_fabricante;
    private List<String> aviones;
}