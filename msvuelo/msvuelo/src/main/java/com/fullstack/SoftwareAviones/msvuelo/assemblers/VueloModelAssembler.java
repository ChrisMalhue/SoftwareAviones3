package com.fullstack.SoftwareAviones.msvuelo.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.fullstack.SoftwareAviones.msvuelo.DTO.VueloDTO;
import com.fullstack.SoftwareAviones.msvuelo.controller.VueloController;

@Component
public class VueloModelAssembler implements RepresentationModelAssembler<VueloDTO, EntityModel<VueloDTO>> {

    @Override
    public EntityModel<VueloDTO> toModel(VueloDTO vuelo) {
        return EntityModel.of(vuelo,
            linkTo(methodOn(VueloController.class).buscarPorId(vuelo.getID_vuelo())).withSelfRel(),
            linkTo(methodOn(VueloController.class).obtenerTodos()).withRel("vuelos"),
            linkTo(methodOn(VueloController.class).eliminarVuelo(vuelo.getID_vuelo())).withRel("eliminar"),
            linkTo(methodOn(VueloController.class).patchVuelo(vuelo.getID_vuelo(), null)).withRel("patch"),
            linkTo(methodOn(VueloController.class).actualizarVuelo(vuelo.getID_vuelo(), null)).withRel("actualizar"));
    }
}
