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
public class OrigenDTO extends RepresentationModel<OrigenDTO> {
    private Integer id_origen;
    private String pais_origen;
    private List<String> aviones;
}
