package com.fullstack.SoftwareAviones.msavion.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.fullstack.SoftwareAviones.msavion.DTO.AvionDTO;
import com.fullstack.SoftwareAviones.msavion.assemblers.AvionModelAssembler;
import com.fullstack.SoftwareAviones.msavion.model.Avion;
import com.fullstack.SoftwareAviones.msavion.services.AvionService;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/aviones")
@Tag(name = "Aviones", description = "Operaciones relacionadas con los aviones")
public class AvionController {

    @Autowired
    private AvionService avionService;

    @Autowired
    private AvionModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los aviones", description = "Obtiene una lista de todos los aviones")
    public ResponseEntity<?> obtenerTodos() {
        List<EntityModel<AvionDTO>> aviones = avionService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (aviones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(CollectionModel.of(aviones,
                linkTo(methodOn(AvionController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un avion por su ID", description = "Obtiene el avion por el ID ingresado")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            AvionDTO avionDTO = avionService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(avionDTO));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping(value = "/matricula/{matricula}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un avion por su Matricula", description = "Obtiene el avion por la Matricula ingresada")
    public ResponseEntity<?> buscarPorMatricula(@PathVariable String matricula) {
        try {
            List<EntityModel<AvionDTO>> aviones = avionService.buscarPorMatricula(matricula)
                    .stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            if (aviones.isEmpty()) {
                return new ResponseEntity<>("No se encontraron aviones con la matrícula: " + matricula, HttpStatus.NOT_FOUND);
            }

            return ResponseEntity.ok(CollectionModel.of(aviones,
                    linkTo(methodOn(AvionController.class).buscarPorMatricula(matricula)).withSelfRel(),
                    linkTo(methodOn(AvionController.class).obtenerTodos()).withRel("aviones")));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Agregar un avion", description = "Agrega un avion a la base de datos")
    public ResponseEntity<?> agregarAvion(@Valid @RequestBody Avion avion) {
        try {
            AvionDTO dto = avionService.guardarAvion(avion);
            return ResponseEntity
                .created(linkTo(methodOn(AvionController.class).buscarPorId(dto.getID_avion())).toUri())
                .body(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un avion", description = "Elimina un avion de la base de datos")
    public ResponseEntity<?> eliminarAvion(@PathVariable Integer id) {
        try {
            String mensaje = avionService.eliminar(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar una columna de avion", description = "Actualiza el atributo del avion solicitado")
    public ResponseEntity<?> patchAvion(@PathVariable Integer id, @RequestBody Avion avion) {
        try {
            AvionDTO dto = avionService.patchAvion(id, avion);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar el avion completo", description = "Actualiza el avion completo con sus atributos")
    public ResponseEntity<?> actualizarAvion(@PathVariable Integer id, @Valid @RequestBody Avion avion) {
        try {
            avion.setID_avion(id);
            AvionDTO dto = avionService.actualizarAvion(id, avion);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
