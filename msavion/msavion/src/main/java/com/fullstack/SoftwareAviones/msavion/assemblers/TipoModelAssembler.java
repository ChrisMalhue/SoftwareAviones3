package com.fullstack.SoftwareAviones.msavion.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.fullstack.SoftwareAviones.msavion.DTO.TipoDTO;
import com.fullstack.SoftwareAviones.msavion.controller.TipoController;

@Component
public class TipoModelAssembler implements RepresentationModelAssembler<TipoDTO, EntityModel<TipoDTO>> {

    @Override
    public EntityModel<TipoDTO> toModel(TipoDTO tipo) {
        return EntityModel.of(tipo,
            linkTo(methodOn(TipoController.class).buscarPorId(tipo.getId_tipo())).withSelfRel(),
            linkTo(methodOn(TipoController.class).obtenerTodos()).withRel("tipos"),
            linkTo(methodOn(TipoController.class).eliminarTipo(tipo.getId_tipo())).withRel("eliminar"),
            linkTo(methodOn(TipoController.class).patchTipo(tipo.getId_tipo(), null)).withRel("patch"),
            linkTo(methodOn(TipoController.class).actualizarTipo(tipo.getId_tipo(), null)).withRel("actualizar"));
    }
}
