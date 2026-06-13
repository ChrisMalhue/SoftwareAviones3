package com.fullstack.SoftwareAviones.msubicacion.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.fullstack.SoftwareAviones.msubicacion.DTO.RegionDTO;
import com.fullstack.SoftwareAviones.msubicacion.controller.RegionController;

@Component
public class RegionModelAssembler implements RepresentationModelAssembler<RegionDTO, EntityModel<RegionDTO>> {

    @Override
    public EntityModel<RegionDTO> toModel(RegionDTO region) {
        return EntityModel.of(region,
            linkTo(methodOn(RegionController.class).buscarPorId(region.getID_region())).withSelfRel(),
            linkTo(methodOn(RegionController.class).obtenerTodos()).withRel("regiones"),
            linkTo(methodOn(RegionController.class).eliminarRegion(region.getID_region())).withRel("eliminar"),
            linkTo(methodOn(RegionController.class).editarRegion(region.getID_region(), null)).withRel("patch"),
            linkTo(methodOn(RegionController.class).actualizarRegion(region.getID_region(), null)).withRel("actualizar"));
    }
}
