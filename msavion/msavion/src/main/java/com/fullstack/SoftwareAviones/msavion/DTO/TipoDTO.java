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
public class TipoDTO extends RepresentationModel<TipoDTO> {
    private Integer id_tipo;
    private String tipo;
    private List<String> aviones;
}
