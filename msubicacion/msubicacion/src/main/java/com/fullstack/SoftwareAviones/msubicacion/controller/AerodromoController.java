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

import com.fullstack.SoftwareAviones.msubicacion.DTO.AerodromoDTO;
import com.fullstack.SoftwareAviones.msubicacion.model.Aerodromo;
import com.fullstack.SoftwareAviones.msubicacion.services.AerodromoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/aerodromos")
public class AerodromoController {

    @Autowired
    private AerodromoService aerodromoService;

    @GetMapping
    public ResponseEntity<List<AerodromoDTO>> obtenerTodos() {
        List<AerodromoDTO> aerodromos = aerodromoService.obtenerTodos();
        if (aerodromos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(aerodromos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AerodromoDTO> buscarPorId(@PathVariable Integer id) {
        try {
            AerodromoDTO aerodromo = aerodromoService.buscarPorId(id);
            return new ResponseEntity<>(aerodromo, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Aerodromo> agregarAerodromo(@Valid @RequestBody Aerodromo aerodromo) {
        try {
            Aerodromo nuevo = aerodromoService.guardarAerodromo(aerodromo);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarAerodromo(@PathVariable Integer id) {
        String resultado = aerodromoService.eliminar(id);
        if (resultado.contains("exitosamente") || resultado.contains("retirado")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
        return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<Aerodromo> patchAerodromo(@PathVariable Integer id, @RequestBody Aerodromo aerodromo) {
        try {
            Aerodromo aerodromoEditado = aerodromoService.patchAerodromo(id, aerodromo);
            return new ResponseEntity<>(aerodromoEditado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }   

    @PutMapping("/{id}")
    public ResponseEntity<Aerodromo> actualizarAerodromo(@PathVariable Integer id, @Valid @RequestBody Aerodromo aerodromo) {
        try {
            Aerodromo actualizado = aerodromoService.actualizarAerodromo(id, aerodromo);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }    

}
