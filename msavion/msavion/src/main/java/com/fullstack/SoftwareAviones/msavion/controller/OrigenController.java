package com.fullstack.SoftwareAviones.msavion.controller;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import com.fullstack.SoftwareAviones.msavion.DTO.OrigenDTO;
import com.fullstack.SoftwareAviones.msavion.assemblers.OrigenModelAssembler;
import com.fullstack.SoftwareAviones.msavion.model.Origen;
import com.fullstack.SoftwareAviones.msavion.services.OrigenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/origenes")
@Tag(name = "Origenes", description = "Operaciones relacionadas con los origenes")
public class OrigenController {

    @Autowired
    private OrigenService origenService;

    @Autowired
    private OrigenModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los origenes", description = "Obtiene una lista de todos los origenes")    
    public ResponseEntity<?> obtenerTodos() {
        List<EntityModel<OrigenDTO>> origenes = origenService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (origenes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(CollectionModel.of(origenes,
                linkTo(methodOn(OrigenController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un origen por su ID", description = "Obtiene el origen por el ID ingresado")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            OrigenDTO dto = origenService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Agregar un origen", description = "Agrega un origen a la base de datos")
    public ResponseEntity<?> agregarOrigen(@Valid @RequestBody Origen origen) {
        try {
            OrigenDTO dto = origenService.guardarOrigen(origen);
            return ResponseEntity
                .created(linkTo(methodOn(OrigenController.class).buscarPorId(dto.getId_origen())).toUri())
                .body(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un origen", description = "Elimina un origen de la base de datos")
    public ResponseEntity<?> eliminarOrigen(@PathVariable Integer id) {
        try {
            String mensaje = origenService.eliminar(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar una columna de origen", description = "Actualiza el atributo del origen solicitado")
    public ResponseEntity<?> patchOrigen(@PathVariable Integer id, @RequestBody Origen origen) {
        try {
            OrigenDTO dto = origenService.patchOrigen(id, origen);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar el origen completo", description = "Actualiza el origen completo con sus atributos")
    public ResponseEntity<?> actualizarOrigen(@PathVariable Integer id, @Valid @RequestBody Origen origen) {
        try {
            origen.setId_origen(id);
            OrigenDTO dto = origenService.actualizarOrigen(id, origen);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
