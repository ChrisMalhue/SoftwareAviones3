package com.fullstack.SoftwareAviones.mspiloto.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.fullstack.SoftwareAviones.mspiloto.DTO.PilotoDTO;
import com.fullstack.SoftwareAviones.mspiloto.controller.PilotoController;

@Component
public class PilotoModelAssembler implements RepresentationModelAssembler<PilotoDTO, EntityModel<PilotoDTO>> {

    @Override
    public EntityModel<PilotoDTO> toModel(PilotoDTO piloto) {
        return EntityModel.of(piloto,
            linkTo(methodOn(PilotoController.class).buscarPorId(piloto.getID_piloto())).withSelfRel(),
            linkTo(methodOn(PilotoController.class).obtenerTodos()).withRel("pilotos"),
            linkTo(methodOn(PilotoController.class).eliminarPiloto(piloto.getID_piloto())).withRel("eliminar"),
            linkTo(methodOn(PilotoController.class).patchPiloto(piloto.getID_piloto(), null)).withRel("patch"),
            linkTo(methodOn(PilotoController.class).actualizarPiloto(piloto.getID_piloto(), null)).withRel("actualizar"));
    }
}
