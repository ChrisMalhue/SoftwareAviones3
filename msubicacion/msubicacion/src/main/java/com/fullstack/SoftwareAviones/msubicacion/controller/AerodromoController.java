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

import com.fullstack.SoftwareAviones.msubicacion.DTO.AerodromoDTO;
import com.fullstack.SoftwareAviones.msubicacion.assemblers.AerodromoModelAssembler;
import com.fullstack.SoftwareAviones.msubicacion.model.Aerodromo;
import com.fullstack.SoftwareAviones.msubicacion.services.AerodromoService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/aerodromos")
@Tag(name = "Aerodromos", description = "Operaciones relacionadas con los aerodromos")
public class AerodromoController {

    @Autowired
    private AerodromoService aerodromoService;

    @Autowired
    private AerodromoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los aerodromos", description = "Obtiene una lista de todos los aerodromos")
    public ResponseEntity<?> obtenerTodos() {
        List<EntityModel<AerodromoDTO>> aerodromos = aerodromoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        if (aerodromos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(CollectionModel.of(aerodromos,
                linkTo(methodOn(AerodromoController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un aerodromo por su ID", description = "Obtiene el aerodromo por el ID ingresado")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            AerodromoDTO dto = aerodromoService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Agregar un aerodromo", description = "Agrega un aerodromo a la base de datos")
    public ResponseEntity<?> agregarAerodromo(@Valid @RequestBody Aerodromo aerodromo) {
        try {
            AerodromoDTO dto = aerodromoService.guardarAerodromo(aerodromo);
            return ResponseEntity
                .created(linkTo(methodOn(AerodromoController.class).buscarPorId(dto.getID_aerodromo())).toUri())
                .body(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un aerodromo", description = "Elimina un aerodromo de la base de datos")
    public ResponseEntity<?> eliminarAerodromo(@PathVariable Integer id) {
        String resultado = aerodromoService.eliminar(id);
        if (resultado.contains("exitosamente") || resultado.contains("retirado")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
        return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
    }
    
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar una columna del aerodromo", description = "Actualiza el atributo del aerodromo solicitado")
    public ResponseEntity<?> patchAerodromo(@PathVariable Integer id, @RequestBody Aerodromo aerodromo) {
        try {
            AerodromoDTO dto = aerodromoService.patchAerodromo(id, aerodromo);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }   

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar el aerodromo completo", description = "Actualiza el aerodromo completo con sus atributos")
    public ResponseEntity<?> actualizarAerodromo(@PathVariable Integer id, @Valid @RequestBody Aerodromo aerodromo) {
        try {
            aerodromo.setID_aerodromo(id);
            AerodromoDTO dto = aerodromoService.actualizarAerodromo(id, aerodromo);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }   

}
