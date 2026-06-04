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

import com.fullstack.SoftwareAviones.msvuelo.DTO.AvionesDTO;
import com.fullstack.SoftwareAviones.msvuelo.model.Aviones;
import com.fullstack.SoftwareAviones.msvuelo.services.AvionesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/avionesPiloto")
public class AvionesController {

    @Autowired
    private AvionesService avionesService;

    @GetMapping
    public ResponseEntity<List<AvionesDTO>> obtenerTodos() {
        List<AvionesDTO> aviones = avionesService.obtenerTodos();
        if (aviones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(aviones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvionesDTO> buscarPorId(@PathVariable Integer id) {
        try {
            AvionesDTO aviones = avionesService.buscarPorId(id);
            return new ResponseEntity<>(aviones, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Aviones> agregarAviones(@Valid @RequestBody Aviones aviones) {
        try {
            Aviones nuevoRegistro = avionesService.guardarAviones(aviones);
            return new ResponseEntity<>(nuevoRegistro, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aviones> actualizarAviones(@PathVariable Integer id, @Valid @RequestBody Aviones aviones) {
        try {
            Aviones avionesActualizado = avionesService.actualizarAviones(id, aviones);
            return new ResponseEntity<>(avionesActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Aviones> editarAviones(@PathVariable Integer id, @RequestBody Aviones aviones) {
        try {
            Aviones avionesEditado = avionesService.actualizarAviones(id, aviones);
            return new ResponseEntity<>(avionesEditado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarAviones(@PathVariable Integer id) {
        String resultado = avionesService.eliminar(id);
        if (resultado.contains("correctamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}