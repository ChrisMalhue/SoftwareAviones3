package com.fullstack.SoftwareAviones.msvuelo.controller;

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

import com.fullstack.SoftwareAviones.msvuelo.DTO.AvionesDTO;
import com.fullstack.SoftwareAviones.msvuelo.assemblers.AvionesModelAssembler;
import com.fullstack.SoftwareAviones.msvuelo.model.Aviones;
import com.fullstack.SoftwareAviones.msvuelo.services.AvionesService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/avionesPiloto")
@Tag(name = "AvionesPiloto", description = "Operaciones relacionadas con la asignación de aviones a pilotos")
public class AvionesController {

    @Autowired
    private AvionesService avionesService;

    @Autowired
    private AvionesModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los registros", description = "Obtiene una lista de todas las asignaciones de aviones a pilotos")
    public ResponseEntity<?> obtenerTodos() {
        List<EntityModel<AvionesDTO>> aviones = avionesService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (aviones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(CollectionModel.of(aviones,
                linkTo(methodOn(AvionesController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un registro por su ID", description = "Obtiene la asignación avion-piloto por el ID ingresado")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            AvionesDTO dto = avionesService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Agregar una asignación", description = "Asigna un avion a un piloto")
    public ResponseEntity<?> agregarAviones(@Valid @RequestBody Aviones aviones) {
        try {
            AvionesDTO dto = avionesService.guardarAviones(aviones);
            return ResponseEntity
                .created(linkTo(methodOn(AvionesController.class).buscarPorId(dto.getID_aviones())).toUri())
                .body(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar una asignación completa", description = "Actualiza la asignación avion-piloto completa")
    public ResponseEntity<?> actualizarAviones(@PathVariable Integer id, @Valid @RequestBody Aviones aviones) {
        try {
            aviones.setID_aviones(id);
            AvionesDTO dto = avionesService.actualizarAviones(id, aviones);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Editar una asignación", description = "Edita la asignación avion-piloto solicitada")
    public ResponseEntity<?> editarAviones(@PathVariable Integer id, @RequestBody Aviones aviones) {
        try {
            AvionesDTO dto = avionesService.actualizarAviones(id, aviones);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar una asignación", description = "Elimina la asignación avion-piloto")
    public ResponseEntity<?> eliminarAviones(@PathVariable Integer id) {
        String resultado = avionesService.eliminar(id);
        if (resultado.contains("correctamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}