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

import com.fullstack.SoftwareAviones.msavion.DTO.AvionDTO;
import com.fullstack.SoftwareAviones.msavion.assemblers.AvionModelAssembler;
import com.fullstack.SoftwareAviones.msavion.model.Avion;
import com.fullstack.SoftwareAviones.msavion.services.AvionService;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;

import java.util.stream.Collectors;

import javax.swing.text.html.parser.Entity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/aviones")
public class AvionController {

    @Autowired
    private AvionService avionService;

    @Autowired
    private AvionModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<AvionDTO>> obtenerTodos() {
        List<EntityModel<AvionDTO>> aviones = avionService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(aviones,
                    linkTo(methodOn(AvionController.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<AvionDTO> buscarPorId(@PathVariable Integer id){
        AvionDTO avionDTO = avionService.buscarPorId(id);
        return assembler.toModel(avionDTO);
    }
    
    @GetMapping(value = "/matricula/{matricula}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<AvionDTO>> buscarPorMatricula(@PathVariable String matricula) {
        List<EntityModel<AvionDTO>> aviones = avionService.buscarPorMatricula(matricula)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (aviones.isEmpty()) {
            return CollectionModel.empty();
        }

        return CollectionModel.of(aviones,
                linkTo(methodOn(AvionController.class).buscarPorMatricula(matricula)).withSelfRel(),
                linkTo(methodOn(AvionController.class).obtenerTodos()).withRel("aviones"));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Avion>> agregarAvion(@Valid @RequestBody Avion avion) {
        Avion newAvion = avionService.guardarAvion(avion);
        return ResponseEntity
                .created(linkTo(methodOn(AvionController.class).buscarPorId(newAvion.getID_avion())).toUri())
                .body(assembler.toModel(newAvion));
    }

/*  @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Carrera>> createCarrera(@RequestBody Carrera carrera) {
        Carrera newCarrera = carreraService.save(carrera);
        return ResponseEntity
                .created(linkTo(methodOn(CarreraControllerV2.class).getCarreraByCodigo(newCarrera.getCodigo())).toUri())
                .body(assembler.toModel(newCarrera));
    } */

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarAvion(@PathVariable Integer id) {
        String resultado = avionService.eliminar(id);
        if (resultado.contains("correctamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<Avion> patchAvion(@PathVariable Integer id, @RequestBody Avion avion) {
        try {
            Avion avionEditado = avionService.patchAvion(id, avion);
            return new ResponseEntity<>(avionEditado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Avion> actualizarAvion(@PathVariable Integer id, @Valid @RequestBody Avion avion) {
        try {
            Avion avionActualizado = avionService.actualizarAvion(id, avion);
            return new ResponseEntity<>(avionActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }    



    

}
