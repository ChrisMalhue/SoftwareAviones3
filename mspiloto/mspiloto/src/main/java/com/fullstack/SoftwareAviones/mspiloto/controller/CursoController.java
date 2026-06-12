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

import com.fullstack.SoftwareAviones.mspiloto.DTO.CursoDTO;
import com.fullstack.SoftwareAviones.mspiloto.assemblers.CursoModelAssembler;
import com.fullstack.SoftwareAviones.mspiloto.model.Curso;
import com.fullstack.SoftwareAviones.mspiloto.services.CursoService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cursos")
@Tag(name = "Cursos", description = "Operaciones relacionadas con los cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CursoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los cursos", description = "Obtiene una lista de todos los cursos")
    public ResponseEntity<?> obtenerTodos() {
        List<EntityModel<CursoDTO>> cursos = cursoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (cursos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(CollectionModel.of(cursos,
                linkTo(methodOn(CursoController.class).obtenerTodos()).withSelfRel()));
    } 

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un curso por su ID", description = "Obtiene el curso por el ID ingresado")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            CursoDTO dto = cursoService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Agregar un curso", description = "Agrega un curso a la base de datos")
    public ResponseEntity<?> agregarCurso(@Valid @RequestBody Curso curso) {
        try {
            CursoDTO dto = cursoService.guardarCurso(curso);
            return ResponseEntity
                .created(linkTo(methodOn(CursoController.class).buscarPorId(dto.getID_curso())).toUri())
                .body(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un curso", description = "Elimina un curso de la base de datos")
    public ResponseEntity<?> eliminarCurso(@PathVariable Integer id) {
        String resultado = cursoService.eliminar(id);
        if (resultado.contains("correctamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
    
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar una columna del curso", description = "Actualiza el atributo del curso solicitado")
    public ResponseEntity<?> patchCurso(@PathVariable Integer id, @RequestBody Curso curso) {
        try {
            CursoDTO dto = cursoService.patchCurso(id, curso);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar el curso completo", description = "Actualiza el curso completo con sus atributos")
    public ResponseEntity<?> actualizarCurso(@PathVariable Integer id, @Valid @RequestBody Curso curso) {
        try {
            curso.setID_curso(id);
            CursoDTO dto = cursoService.actualizarCurso(id, curso);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    } 

}
