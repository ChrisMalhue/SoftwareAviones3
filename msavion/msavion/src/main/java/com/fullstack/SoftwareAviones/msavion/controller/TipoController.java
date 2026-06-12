package com.fullstack.SoftwareAviones.msavion.controller;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.fullstack.SoftwareAviones.msavion.DTO.TipoDTO;
import com.fullstack.SoftwareAviones.msavion.assemblers.TipoModelAssembler;
import com.fullstack.SoftwareAviones.msavion.model.Tipo;
import com.fullstack.SoftwareAviones.msavion.services.TipoService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tipos")
@Tag(name = "Tipos", description = "Operaciones relacionadas con los tipos")
public class TipoController {

    @Autowired
    private TipoService tipoService;

    @Autowired
    private TipoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los tipos", description = "Obtiene una lista de todos los tipos")
    public ResponseEntity<?> obtenerTodos() {
        List<EntityModel<TipoDTO>> tipos = tipoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (tipos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(CollectionModel.of(tipos,
                linkTo(methodOn(TipoController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un tipo por su ID", description = "Obtiene el tipo por el ID ingresado")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            TipoDTO dto = tipoService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Agregar un tipo", description = "Agrega un tipo a la base de datos")
    public ResponseEntity<?> agregarTipo(@Valid @RequestBody Tipo tipo) {
        try {
            TipoDTO dto = tipoService.guardarTipo(tipo);
            return ResponseEntity
                .created(linkTo(methodOn(TipoController.class).buscarPorId(dto.getId_tipo())).toUri())
                .body(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }  

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un tipo", description = "Elimina un tipo de la base de datos")
    public ResponseEntity<?> eliminarTipo(@PathVariable Integer id) {
        try {
            String mensaje = tipoService.eliminar(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar una columna de tipo", description = "Actualiza el atributo del tipo solicitado")
    public ResponseEntity<?> patchTipo(@PathVariable Integer id, @RequestBody Tipo tipo) {
        try {
            TipoDTO dto = tipoService.patchTipo(id, tipo);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar el tipo completo", description = "Actualiza el tipo completo con sus atributos")
    public ResponseEntity<?> actualizarTipo(@PathVariable Integer id, @Valid @RequestBody Tipo tipo) {
        try {
            tipo.setId_tipo(id);
            TipoDTO dto = tipoService.actualizarTipo(id, tipo);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }  

}
