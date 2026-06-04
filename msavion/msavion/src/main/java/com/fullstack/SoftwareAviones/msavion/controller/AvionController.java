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
import com.fullstack.SoftwareAviones.msavion.model.Avion;
import com.fullstack.SoftwareAviones.msavion.services.AvionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/aviones")
public class AvionController {

    @Autowired
    private AvionService avionService;

    @GetMapping
    public ResponseEntity<List<AvionDTO>> obtenerTodos() {
        List<AvionDTO> aviones = avionService.obtenerTodos();
        if (aviones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(aviones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvionDTO> buscarPorId(@PathVariable Integer id){
        try {
            AvionDTO avion = avionService.buscarPorId(id);
            return new ResponseEntity<>(avion, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<List<AvionDTO>> buscarPorMatricula(@PathVariable String matricula){
        List<AvionDTO> aviones = avionService.buscarPorMatricula(matricula);
        if (aviones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(aviones, HttpStatus.OK);
    }    

    @PostMapping
    public ResponseEntity<Avion> agregarAvion(@Valid @RequestBody Avion avion) {
        try {
            Avion nuevoAvion = avionService.guardarAvion(avion);
            return new ResponseEntity<>(nuevoAvion, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

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
