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

import com.fullstack.SoftwareAviones.msubicacion.DTO.RegionDTO;
import com.fullstack.SoftwareAviones.msubicacion.model.Region;
import com.fullstack.SoftwareAviones.msubicacion.services.RegionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/regiones")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping
    public ResponseEntity<List<RegionDTO>> obtenerTodos() {
        List<RegionDTO> regiones = regionService.obtenerTodos();
        if (regiones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(regiones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> buscarPorId(@PathVariable Integer id) {
        try {
            RegionDTO region = regionService.buscarPorId(id);
            return new ResponseEntity<>(region, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping
    public ResponseEntity<Region> agregarRegion(@Valid @RequestBody Region region) {
        try {
            Region nuevaRegion = regionService.guardarRegion(region);
            return new ResponseEntity<>(nuevaRegion, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRegion(@PathVariable Integer id) {
        String resultado = regionService.eliminar(id);
        if (resultado.contains("correctamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }  
    
    @PatchMapping("/{id}")
    public ResponseEntity<Region> editarRegion(@PathVariable Integer id, @RequestBody Region region) {
        try {
            Region regionEditada = regionService.patchRegion(id, region);
            return new ResponseEntity<>(regionEditada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Region> actualizarRegion(@PathVariable Integer id, @Valid @RequestBody Region region) {
        try {
            Region regionActualizada = regionService.actualizarRegion(id, region);
            return new ResponseEntity<>(regionActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    

}
