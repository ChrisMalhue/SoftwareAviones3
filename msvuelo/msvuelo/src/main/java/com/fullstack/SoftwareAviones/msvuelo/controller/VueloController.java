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

import com.fullstack.SoftwareAviones.msvuelo.DTO.VueloDTO;
import com.fullstack.SoftwareAviones.msvuelo.assemblers.VueloModelAssembler;
import com.fullstack.SoftwareAviones.msvuelo.model.Vuelo;
import com.fullstack.SoftwareAviones.msvuelo.services.VueloService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/vuelos")
@Tag(name = "Vuelos", description = "Operaciones relacionadas con los vuelos")
public class VueloController {

    @Autowired
    private VueloService vueloService;

    @Autowired
    private VueloModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los vuelos", description = "Obtiene una lista de todos los vuelos")
    public ResponseEntity<?> obtenerTodos() {
        List<EntityModel<VueloDTO>> vuelos = vueloService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (vuelos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(CollectionModel.of(vuelos,
                linkTo(methodOn(VueloController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un vuelo por su ID", description = "Obtiene el vuelo por el ID ingresado")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            VueloDTO dto = vueloService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Agregar un vuelo", description = "Agrega un vuelo a la base de datos")
    public ResponseEntity<?> agregarVuelo(@Valid @RequestBody Vuelo vuelo) {
        try {
            VueloDTO dto = vueloService.agregarVuelo(vuelo);
            return ResponseEntity
                .created(linkTo(methodOn(VueloController.class).buscarPorId(dto.getID_vuelo())).toUri())
                .body(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar un vuelo completo", description = "Actualiza el vuelo completo con sus atributos")
    public ResponseEntity<?> actualizarVuelo(@PathVariable Integer id, @Valid @RequestBody Vuelo vuelo) {
        try {
            vuelo.setID_vuelo(id);
            VueloDTO dto = vueloService.actualizarVuelo(id, vuelo);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar una columna del vuelo", description = "Actualiza el atributo del vuelo solicitado")
    public ResponseEntity<?> patchVuelo(@PathVariable Integer id, @RequestBody Vuelo vuelo) {
        try {
            VueloDTO dto = vueloService.patchVuelo(id, vuelo);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un vuelo", description = "Elimina un vuelo de la base de datos")
    public ResponseEntity<?> eliminarVuelo(@PathVariable Integer id) {
        String resultado = vueloService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}