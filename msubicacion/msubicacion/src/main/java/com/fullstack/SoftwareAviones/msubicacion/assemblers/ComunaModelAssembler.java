package com.fullstack.SoftwareAviones.msubicacion.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.fullstack.SoftwareAviones.msubicacion.DTO.ComunaDTO;
import com.fullstack.SoftwareAviones.msubicacion.controller.ComunaController;

@Component
public class ComunaModelAssembler implements RepresentationModelAssembler<ComunaDTO, EntityModel<ComunaDTO>> {

    @Override
    public EntityModel<ComunaDTO> toModel(ComunaDTO comuna) {
        return EntityModel.of(comuna,
            linkTo(methodOn(ComunaController.class).buscarPorId(comuna.getID_comuna())).withSelfRel(),
            linkTo(methodOn(ComunaController.class).obtenerTodos()).withRel("comunas"),
            linkTo(methodOn(ComunaController.class).eliminarComuna(comuna.getID_comuna())).withRel("eliminar"),
            linkTo(methodOn(ComunaController.class).patchComuna(comuna.getID_comuna(), null)).withRel("patch"),
            linkTo(methodOn(ComunaController.class).actualizarComuna(comuna.getID_comuna(), null)).withRel("actualizar"));
    }
}
