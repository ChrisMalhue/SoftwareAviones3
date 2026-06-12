package com.fullstack.SoftwareAviones.mspiloto.controller;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.SoftwareAviones.mspiloto.DTO.CursosDTO;
import com.fullstack.SoftwareAviones.mspiloto.assemblers.CursosModelAssembler;
import com.fullstack.SoftwareAviones.mspiloto.model.Cursos;
import com.fullstack.SoftwareAviones.mspiloto.services.CursosService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cursos_piloto")
@Tag(name = "CursosPiloto", description = "Operaciones relacionadas con la asignación de cursos a pilotos")
public class CursosController {

    @Autowired
    private CursosService cursosService;

    @Autowired
    private CursosModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los registros", description = "Obtiene una lista de todas las asignaciones de cursos a pilotos")
    public ResponseEntity<?> obtenerTodos() {
        List<EntityModel<CursosDTO>> cursos = cursosService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (cursos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(CollectionModel.of(cursos,
                linkTo(methodOn(CursosController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un registro por su ID", description = "Obtiene la asignación de curso-piloto por el ID ingresado")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            CursosDTO dto = cursosService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    } 
    
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Agregar una asignación", description = "Asigna un curso a un piloto")
    public ResponseEntity<?> agregarCursos(@Valid @RequestBody Cursos cursos) {
        try {
            CursosDTO dto = cursosService.guardarCursos(cursos);
            return ResponseEntity
                .created(linkTo(methodOn(CursosController.class).buscarPorId(dto.getID_cursos())).toUri())
                .body(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar una asignación", description = "Elimina la asignación de curso-piloto")
    public ResponseEntity<?> eliminarCursos(@PathVariable Integer id) {
        String resultado = cursosService.eliminar(id);
        if (resultado.contains("correctamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
    // Lo deje como estaba ya que un patch aqui no haria mucho sentido, ya que 
    // si se cambia el piloto o el curso se esta creando una relacion diferente
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Editar una asignación", description = "Edita la asignación de curso-piloto solicitada")
    public ResponseEntity<?> editarCursos(@PathVariable Integer id, @Valid @RequestBody Cursos cursos) {
        try {
            cursos.setID_cursos(id);
            CursosDTO dto = cursosService.guardarCursos(cursos);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar una asignación completa", description = "Actualiza la asignación de curso-piloto completa")
    public ResponseEntity<?> actualizarCursos(@PathVariable Integer id, @Valid @RequestBody Cursos cursos) {
        try {
            CursosDTO dto = cursosService.actualizarCursos(id, cursos);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    

}
