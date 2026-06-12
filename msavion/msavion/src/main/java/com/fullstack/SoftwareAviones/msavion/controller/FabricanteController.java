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

import com.fullstack.SoftwareAviones.msavion.DTO.FabricanteDTO;
import com.fullstack.SoftwareAviones.msavion.assemblers.FabricanteModelAssembler;
import com.fullstack.SoftwareAviones.msavion.model.Fabricante;
import com.fullstack.SoftwareAviones.msavion.services.FabricanteService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/fabricantes")
@Tag(name = "Fabricantes", description = "Operaciones relacionadas con los fabricantes")
public class FabricanteController {

    @Autowired
    private FabricanteService fabricanteService;

    @Autowired
    private FabricanteModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los fabricantes", description = "Obtiene una lista de todos los fabricantes")
    public ResponseEntity<?> obtenerTodos() {
        List<EntityModel<FabricanteDTO>> fabricantes = fabricanteService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (fabricantes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(CollectionModel.of(fabricantes,
                linkTo(methodOn(FabricanteController.class).obtenerTodos()).withSelfRel()));
    }    

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un fabricante por su ID", description = "Obtiene el fabricante por el ID ingresado")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            FabricanteDTO dto = fabricanteService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        }catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Agregar un fabricante", description = "Agrega un fabricante a la base de datos")
    public ResponseEntity<?> agregarFabricante(@Valid @RequestBody Fabricante fabricante) {
        try {
            FabricanteDTO dto = fabricanteService.guardarFabricante(fabricante);
            return ResponseEntity
                .created(linkTo(methodOn(FabricanteController.class).buscarPorId(dto.getId_fabricante())).toUri())
                .body(assembler.toModel(dto));
        }catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un fabricante", description = "Elimina un fabricante de la base de datos")
    public ResponseEntity<?> eliminarFabricante(@PathVariable Integer id) {
        try {
            String mensaje = fabricanteService.eliminar(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    } 

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar una columna de fabricante", description = "Actualiza el atributo del fabricante solicitado")
    public ResponseEntity<?> patchFabricante(@PathVariable Integer id, @RequestBody Fabricante fabricante) {
        try {
            FabricanteDTO dto = fabricanteService.patchFabricante(id, fabricante);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar el fabricante completo", description = "Actualiza el fabricante completo con sus atributos")
    public ResponseEntity<?> actualizarFabricante(@PathVariable Integer id, @Valid @RequestBody Fabricante fabricante) {
        try {
            fabricante.setId_fabricante(id);
            FabricanteDTO dto = fabricanteService.actualizarFabricante(id, fabricante);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
}
