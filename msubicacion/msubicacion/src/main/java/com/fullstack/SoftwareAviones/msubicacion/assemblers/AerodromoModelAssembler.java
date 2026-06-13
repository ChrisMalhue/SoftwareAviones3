package com.fullstack.SoftwareAviones.msubicacion.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.fullstack.SoftwareAviones.msubicacion.DTO.AerodromoDTO;
import com.fullstack.SoftwareAviones.msubicacion.controller.AerodromoController;

@Component
public class AerodromoModelAssembler implements RepresentationModelAssembler<AerodromoDTO, EntityModel<AerodromoDTO>> {

    @Override
    public EntityModel<AerodromoDTO> toModel(AerodromoDTO aerodromo) {
        return EntityModel.of(aerodromo,
            linkTo(methodOn(AerodromoController.class).buscarPorId(aerodromo.getID_aerodromo())).withSelfRel(),
            linkTo(methodOn(AerodromoController.class).obtenerTodos()).withRel("aerodromos"),
            linkTo(methodOn(AerodromoController.class).eliminarAerodromo(aerodromo.getID_aerodromo())).withRel("eliminar"),
            linkTo(methodOn(AerodromoController.class).patchAerodromo(aerodromo.getID_aerodromo(), null)).withRel("patch"),
            linkTo(methodOn(AerodromoController.class).actualizarAerodromo(aerodromo.getID_aerodromo(), null)).withRel("actualizar"));
    }
}
