package com.fullstack.SoftwareAviones.msubicacion.controller;
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

import com.fullstack.SoftwareAviones.msubicacion.DTO.RegionDTO;
import com.fullstack.SoftwareAviones.msubicacion.assemblers.RegionModelAssembler;
import com.fullstack.SoftwareAviones.msubicacion.model.Region;
import com.fullstack.SoftwareAviones.msubicacion.services.RegionService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/regiones")
@Tag(name = "Regiones", description = "Operaciones relacionadas con las regiones")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @Autowired
    private RegionModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todas las regiones", description = "Obtiene una lista de todas las regiones")
    public ResponseEntity<?> obtenerTodos() {
        List<EntityModel<RegionDTO>> regiones = regionService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (regiones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(CollectionModel.of(regiones,
                linkTo(methodOn(RegionController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener una región por su ID", description = "Obtiene la región por el ID ingresado")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            RegionDTO dto = regionService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Agregar una región", description = "Agrega una región a la base de datos")
    public ResponseEntity<?> agregarRegion(@Valid @RequestBody Region region) {
        try {
            RegionDTO dto = regionService.guardarRegion(region);
            return ResponseEntity
                .created(linkTo(methodOn(RegionController.class).buscarPorId(dto.getID_region())).toUri())
                .body(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar una región", description = "Elimina una región de la base de datos")
    public ResponseEntity<?> eliminarRegion(@PathVariable Integer id) {
        String resultado = regionService.eliminar(id);
        if (resultado.contains("correctamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
    
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar una columna de la región", description = "Actualiza el atributo de la región solicitada")
    public ResponseEntity<?> editarRegion(@PathVariable Integer id, @RequestBody Region region) {
        try {
            RegionDTO dto = regionService.patchRegion(id, region);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar la región completa", description = "Actualiza la región completa con sus atributos")
    public ResponseEntity<?> actualizarRegion(@PathVariable Integer id, @Valid @RequestBody Region region) {
        try {
            region.setID_region(id);
            RegionDTO dto = regionService.actualizarRegion(id, region);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
