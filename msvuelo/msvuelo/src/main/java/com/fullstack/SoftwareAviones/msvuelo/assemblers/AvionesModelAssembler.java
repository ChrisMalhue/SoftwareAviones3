package com.fullstack.SoftwareAviones.msvuelo.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.fullstack.SoftwareAviones.msvuelo.DTO.AvionesDTO;
import com.fullstack.SoftwareAviones.msvuelo.controller.AvionesController;

@Component
public class AvionesModelAssembler implements RepresentationModelAssembler<AvionesDTO, EntityModel<AvionesDTO>> {

    @Override
    public EntityModel<AvionesDTO> toModel(AvionesDTO aviones) {
        return EntityModel.of(aviones,
            linkTo(methodOn(AvionesController.class).buscarPorId(aviones.getID_aviones())).withSelfRel(),
            linkTo(methodOn(AvionesController.class).obtenerTodos()).withRel("avionesPiloto"),
            linkTo(methodOn(AvionesController.class).eliminarAviones(aviones.getID_aviones())).withRel("eliminar"),
            linkTo(methodOn(AvionesController.class).editarAviones(aviones.getID_aviones(), null)).withRel("patch"),
            linkTo(methodOn(AvionesController.class).actualizarAviones(aviones.getID_aviones(), null)).withRel("actualizar"));
    }
}
