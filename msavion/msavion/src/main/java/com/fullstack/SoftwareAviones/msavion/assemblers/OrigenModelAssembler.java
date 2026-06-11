package com.fullstack.SoftwareAviones.msavion.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.fullstack.SoftwareAviones.msavion.DTO.OrigenDTO;
import com.fullstack.SoftwareAviones.msavion.controller.OrigenController;

@Component
public class OrigenModelAssembler implements RepresentationModelAssembler<OrigenDTO, EntityModel<OrigenDTO>>{
   
    @Override
    public EntityModel<OrigenDTO> toModel(OrigenDTO origen) {
        return EntityModel.of(origen,
            linkTo(methodOn(OrigenController.class).buscarPorId(origen.getId_origen())).withSelfRel(),
            linkTo(methodOn(OrigenController.class).obtenerTodos()).withRel("origenes"),
            linkTo(methodOn(OrigenController.class).eliminarOrigen(origen.getId_origen())).withRel("eliminar"),
            linkTo(methodOn(OrigenController.class).patchOrigen(origen.getId_origen(), null)).withRel("patch"),
            linkTo(methodOn(OrigenController.class).actualizarOrigen(origen.getId_origen(), null)).withRel("actualizar"));
    }
}
