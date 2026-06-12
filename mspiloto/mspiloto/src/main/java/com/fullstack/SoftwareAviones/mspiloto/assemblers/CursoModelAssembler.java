package com.fullstack.SoftwareAviones.mspiloto.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.fullstack.SoftwareAviones.mspiloto.DTO.CursoDTO;
import com.fullstack.SoftwareAviones.mspiloto.controller.CursoController;

@Component
public class CursoModelAssembler implements RepresentationModelAssembler<CursoDTO, EntityModel<CursoDTO>> {

    @Override
    public EntityModel<CursoDTO> toModel(CursoDTO curso) {
        return EntityModel.of(curso,
            linkTo(methodOn(CursoController.class).buscarPorId(curso.getID_curso())).withSelfRel(),
            linkTo(methodOn(CursoController.class).obtenerTodos()).withRel("cursos"),
            linkTo(methodOn(CursoController.class).eliminarCurso(curso.getID_curso())).withRel("eliminar"),
            linkTo(methodOn(CursoController.class).patchCurso(curso.getID_curso(), null)).withRel("patch"),
            linkTo(methodOn(CursoController.class).actualizarCurso(curso.getID_curso(), null)).withRel("actualizar"));
    }
}
