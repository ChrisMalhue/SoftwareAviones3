package com.fullstack.SoftwareAviones.msavion.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.fullstack.SoftwareAviones.msavion.DTO.AvionDTO;
import com.fullstack.SoftwareAviones.msavion.controller.AvionController;

@Component
public class AvionModelAssembler implements RepresentationModelAssembler<AvionDTO, EntityModel<AvionDTO>>{

    @Override
    public EntityModel<AvionDTO> toModel(AvionDTO avion) {
        return EntityModel.of(avion,
            linkTo(methodOn(AvionController.class).buscarPorId(avion.getID_avion())).withSelfRel(),
            linkTo(methodOn(AvionController.class).obtenerTodos()).withRel("aviones"),
            linkTo(methodOn(AvionController.class).buscarPorMatricula(avion.getMatricula())).withRel("buscar-por-matricula"),
            linkTo(methodOn(AvionController.class).eliminarAvion(avion.getID_avion())).withRel("eliminar"),
            linkTo(methodOn(AvionController.class).patchAvion(avion.getID_avion(), null)).withRel("patch"),
            linkTo(methodOn(AvionController.class).actualizarAvion(avion.getID_avion(), null)).withRel("actualizar"));
                
    }

}
