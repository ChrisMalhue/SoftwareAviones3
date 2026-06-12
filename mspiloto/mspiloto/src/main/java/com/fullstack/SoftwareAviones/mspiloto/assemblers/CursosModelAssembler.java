package com.fullstack.SoftwareAviones.mspiloto.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.fullstack.SoftwareAviones.mspiloto.DTO.CursosDTO;
import com.fullstack.SoftwareAviones.mspiloto.controller.CursosController;

@Component
public class CursosModelAssembler implements RepresentationModelAssembler<CursosDTO, EntityModel<CursosDTO>> {

    @Override
    public EntityModel<CursosDTO> toModel(CursosDTO cursos) {
        return EntityModel.of(cursos,
            linkTo(methodOn(CursosController.class).buscarPorId(cursos.getID_cursos())).withSelfRel(),
            linkTo(methodOn(CursosController.class).obtenerTodos()).withRel("cursos_piloto"),
            linkTo(methodOn(CursosController.class).eliminarCursos(cursos.getID_cursos())).withRel("eliminar"),
            linkTo(methodOn(CursosController.class).editarCursos(cursos.getID_cursos(), null)).withRel("patch"),
            linkTo(methodOn(CursosController.class).actualizarCursos(cursos.getID_cursos(), null)).withRel("actualizar"));
    }
}
