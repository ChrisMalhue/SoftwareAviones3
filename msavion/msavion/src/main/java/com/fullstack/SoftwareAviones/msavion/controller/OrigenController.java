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

import com.fullstack.SoftwareAviones.msavion.DTO.OrigenDTO;
import com.fullstack.SoftwareAviones.msavion.model.Origen;
import com.fullstack.SoftwareAviones.msavion.services.OrigenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/origenes")
public class OrigenController {

    @Autowired
    private OrigenService origenService;

    @GetMapping
    public ResponseEntity<List<OrigenDTO>> obtenerTodos() {
        List<OrigenDTO> origenes = origenService.obtenerTodos();
        if (origenes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(origenes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrigenDTO> buscarPorId(@PathVariable Integer id) {
        try {
            OrigenDTO origen = origenService.buscarPorId(id);
            return new ResponseEntity<>(origen, HttpStatus.OK);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Origen> agregarOrigen(@Valid @RequestBody Origen origen) {
        try {
            Origen nuevo = origenService.guardarOrigen(origen);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }    

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarOrigen(@PathVariable Integer id) {
        String resultado = origenService.eliminar(id);
        if (resultado.contains("exitosamente") || resultado.contains("retirado")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
        return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
    }    

    @PatchMapping("/{id}")
    public ResponseEntity<Origen> patchOrigen(@PathVariable Integer id, @RequestBody Origen origen) {
        try {
            Origen origenEditado = origenService.patchOrigen(id, origen);
            return new ResponseEntity<>(origenEditado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Origen> actualizarOrigen(@PathVariable Integer id,
                                                    @Valid @RequestBody Origen origen) {
        try {
            Origen actualizado = origenService.actualizarOrigen(id, origen);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }    


}
