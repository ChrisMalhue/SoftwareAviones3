package com.fullstack.SoftwareAviones.msvuelo.controller;

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

import com.fullstack.SoftwareAviones.msvuelo.DTO.VueloDTO;
import com.fullstack.SoftwareAviones.msvuelo.model.Vuelo;
import com.fullstack.SoftwareAviones.msvuelo.services.VueloService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/vuelos")
public class VueloController {

    @Autowired
    private VueloService vueloService;

    @GetMapping
    public ResponseEntity<List<VueloDTO>> obtenerTodos() {
        List<VueloDTO> vuelos = vueloService.obtenerTodos();
        if (vuelos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(vuelos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VueloDTO> buscarPorId(@PathVariable Integer id) {
        try {
            VueloDTO vuelo = vueloService.buscarPorId(id);
            return new ResponseEntity<>(vuelo, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Vuelo> agregarVuelo(@Valid @RequestBody Vuelo vuelo) {
        try {
            Vuelo nuevoVuelo = vueloService.agregarVuelo(vuelo);
            return new ResponseEntity<>(nuevoVuelo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vuelo> actualizarVuelo(@PathVariable Integer id, @Valid @RequestBody Vuelo vuelo) {
        try {
            Vuelo vueloActualizado = vueloService.actualizarVuelo(id, vuelo);
            return new ResponseEntity<>(vueloActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Vuelo> patchVuelo(@PathVariable Integer id, @RequestBody Vuelo vuelo) {
        try {
            Vuelo vueloEditado = vueloService.patchVuelo(id, vuelo);
            return new ResponseEntity<>(vueloEditado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarVuelo(@PathVariable Integer id) {
        String resultado = vueloService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}