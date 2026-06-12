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

import com.fullstack.SoftwareAviones.mspiloto.DTO.PilotoDTO;
import com.fullstack.SoftwareAviones.mspiloto.assemblers.PilotoModelAssembler;
import com.fullstack.SoftwareAviones.mspiloto.model.Piloto;
import com.fullstack.SoftwareAviones.mspiloto.services.PilotoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pilotos")
@Tag(name = "Pilotos", description = "Operaciones relacionadas con los pilotos")
public class PilotoController {

    @Autowired
    private PilotoService pilotoService;

    @Autowired
    private PilotoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los pilotos", description = "Obtiene una lista de todos los pilotos")
    public ResponseEntity<?> obtenerTodos() {
        List<EntityModel<PilotoDTO>> pilotos = pilotoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (pilotos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(CollectionModel.of(pilotos,
                linkTo(methodOn(PilotoController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un piloto por su ID", description = "Obtiene el piloto por el ID ingresado")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            PilotoDTO dto = pilotoService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/nombre/{nombre}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar pilotos por nombre", description = "Obtiene una lista de pilotos que coincidan con el nombre ingresado")
    public ResponseEntity<?> buscarPorNombre(@PathVariable String nombre) {
        List<EntityModel<PilotoDTO>> pilotos = pilotoService.buscarPorNombre(nombre).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (pilotos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(CollectionModel.of(pilotos,
                linkTo(methodOn(PilotoController.class).buscarPorNombre(nombre)).withSelfRel(),
                linkTo(methodOn(PilotoController.class).obtenerTodos()).withRel("pilotos")));
    }

    @GetMapping(value = "/horas/{horas}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar pilotos por horas de vuelo mínimas", description = "Obtiene una lista de pilotos que tengan al menos las horas de vuelo ingresadas")
    public ResponseEntity<?> buscarPilotosConHorasMinimas(@PathVariable Integer horas) {
        List<EntityModel<PilotoDTO>> pilotos = pilotoService.buscarPilotosConHorasMinimas(horas).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (pilotos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(CollectionModel.of(pilotos,
                linkTo(methodOn(PilotoController.class).buscarPilotosConHorasMinimas(horas)).withSelfRel(),
                linkTo(methodOn(PilotoController.class).obtenerTodos()).withRel("pilotos")));
    }    

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Agregar un piloto", description = "Agrega un piloto a la base de datos")
    public ResponseEntity<?> agregarPiloto(@Valid @RequestBody Piloto piloto) {
        try {
            PilotoDTO dto = pilotoService.guardarPiloto(piloto);
            return ResponseEntity
                .created(linkTo(methodOn(PilotoController.class).buscarPorId(dto.getID_piloto())).toUri())
                .body(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un piloto", description = "Elimina un piloto de la base de datos")
    public ResponseEntity<?> eliminarPiloto(@PathVariable Integer id) {
        String resultado = pilotoService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar una columna del piloto", description = "Actualiza el atributo del piloto solicitado")
    public ResponseEntity<?> patchPiloto(@PathVariable Integer id, @RequestBody Piloto piloto) {
        try {
            PilotoDTO dto = pilotoService.patchPiloto(id, piloto);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar el piloto completo", description = "Actualiza el piloto completo con sus atributos")
    public ResponseEntity<?> actualizarPiloto(@PathVariable Integer id, @Valid @RequestBody Piloto piloto) {
        try {
            piloto.setID_piloto(id);
            PilotoDTO dto = pilotoService.actualizarPiloto(id, piloto);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    

}
