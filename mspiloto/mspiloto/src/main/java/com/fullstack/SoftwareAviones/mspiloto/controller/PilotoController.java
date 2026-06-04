package com.fullstack.SoftwareAviones.mspiloto.controller;

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

import com.fullstack.SoftwareAviones.mspiloto.DTO.PilotoDTO;
import com.fullstack.SoftwareAviones.mspiloto.model.Piloto;
import com.fullstack.SoftwareAviones.mspiloto.services.PilotoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pilotos")
public class PilotoController {

    @Autowired
    private PilotoService pilotoService;

    @GetMapping
    public ResponseEntity<List<PilotoDTO>> obtenerTodos() {
        List<PilotoDTO> pilotos = pilotoService.obtenerTodos();

        if (pilotos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(pilotos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PilotoDTO> buscarPorId(@PathVariable Integer id) {
        try {
            PilotoDTO piloto = pilotoService.buscarPorId(id);
            return new ResponseEntity<>(piloto, HttpStatus.OK);

        } catch (RuntimeException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }    

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<PilotoDTO>> buscarPorNombre(@PathVariable String nombre) {
        List<PilotoDTO> pilotos = pilotoService.buscarPorNombre(nombre);

        if (pilotos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(pilotos, HttpStatus.OK);
    }

    @GetMapping("/horas/{horas}")
    public ResponseEntity<List<PilotoDTO>> buscarPilotosConHorasMinimas(@PathVariable Integer horas) {
        List<PilotoDTO> pilotos = pilotoService.buscarPilotosConHorasMinimas(horas);

        if (pilotos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(pilotos, HttpStatus.OK);
    }    

    @PostMapping
    public ResponseEntity<Piloto> agregarPiloto(@Valid @RequestBody Piloto piloto) {
        try {

            Piloto nuevoPiloto = pilotoService.guardarPiloto(piloto);
            return new ResponseEntity<>(nuevoPiloto, HttpStatus.CREATED);

        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPiloto(@PathVariable Integer id) {
        String resultado = pilotoService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {

            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Piloto> patchPiloto(@PathVariable Integer id, @RequestBody Piloto piloto) {
        try {
            Piloto pilotoEditado = pilotoService.patchPiloto(id, piloto);
            return new ResponseEntity<>(pilotoEditado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Piloto> actualizarPiloto(@PathVariable Integer id, @Valid @RequestBody Piloto piloto) {
        try {
            Piloto pilotoActualizado = pilotoService.actualizarPiloto(id, piloto);
            return new ResponseEntity<>(pilotoActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    

}
