package com.fullstack.SoftwareAviones.msubicacion.controller;
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

import com.fullstack.SoftwareAviones.msubicacion.DTO.ComunaDTO;
import com.fullstack.SoftwareAviones.msubicacion.model.Comuna;
import com.fullstack.SoftwareAviones.msubicacion.services.ComunaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/comunas")
public class ComunaController {

    @Autowired
    private ComunaService comunaService;

    @GetMapping
    public ResponseEntity<List<ComunaDTO>> obtenerTodos() {
        List<ComunaDTO> comunas = comunaService.obtenerTodos();
        if (comunas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(comunas, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ComunaDTO> buscarPorId(@PathVariable Integer id) {
        try {
            ComunaDTO comuna = comunaService.buscarPorId(id);
            return new ResponseEntity<>(comuna, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Comuna> agregarComuna(@Valid @RequestBody Comuna comuna) {
        try {
            Comuna nuevaComuna = comunaService.guardarComuna(comuna);
            return new ResponseEntity<>(nuevaComuna, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarComuna(@PathVariable Integer id) {
        String resultado = comunaService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }  
    
    @PatchMapping("/{id}")
    public ResponseEntity<Comuna> patchComuna(@PathVariable Integer id, @RequestBody Comuna comuna) {
        try {
            Comuna comunaEditada = comunaService.patchComuna(id, comuna);
            return new ResponseEntity<>(comunaEditada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }  
    
    @PutMapping("/{id}")
    public ResponseEntity<Comuna> actualizarComuna(@PathVariable Integer id, @Valid @RequestBody Comuna comuna) {
        try {
            Comuna comunaActualizada = comunaService.actualizarComuna(id, comuna);
            return new ResponseEntity<>(comunaActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }    
}
