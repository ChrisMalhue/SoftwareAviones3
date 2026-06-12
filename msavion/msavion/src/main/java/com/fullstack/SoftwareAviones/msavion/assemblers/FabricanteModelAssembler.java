package com.fullstack.SoftwareAviones.msavion.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.fullstack.SoftwareAviones.msavion.DTO.FabricanteDTO;
import com.fullstack.SoftwareAviones.msavion.controller.FabricanteController;

@Component
public class FabricanteModelAssembler implements RepresentationModelAssembler<FabricanteDTO, EntityModel<FabricanteDTO>> {

    @Override
    public EntityModel<FabricanteDTO> toModel(FabricanteDTO fabricante) {
        return EntityModel.of(fabricante,
            linkTo(methodOn(FabricanteController.class).buscarPorId(fabricante.getId_fabricante())).withSelfRel(),
            linkTo(methodOn(FabricanteController.class).obtenerTodos()).withRel("fabricantes"),
            linkTo(methodOn(FabricanteController.class).eliminarFabricante(fabricante.getId_fabricante())).withRel("eliminar"),
            linkTo(methodOn(FabricanteController.class).patchFabricante(fabricante.getId_fabricante(), null)).withRel("patch"),
            linkTo(methodOn(FabricanteController.class).actualizarFabricante(fabricante.getId_fabricante(), null)).withRel("actualizar"));
    }
}
