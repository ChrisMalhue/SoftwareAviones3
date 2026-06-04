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

import com.fullstack.SoftwareAviones.mspiloto.DTO.CursosDTO;
import com.fullstack.SoftwareAviones.mspiloto.model.Cursos;
import com.fullstack.SoftwareAviones.mspiloto.services.CursosService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cursos_piloto")
public class CursosController {

    @Autowired
    private CursosService cursosService;

    @GetMapping
    public ResponseEntity<List<CursosDTO>> obtenerTodos() {
        List<CursosDTO> cursos = cursosService.obtenerTodos();
        if (cursos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursosDTO> buscarPorId(@PathVariable Integer id) {
        try {
            CursosDTO cursos = cursosService.buscarPorId(id);
            return new ResponseEntity<>(cursos, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }  
    
    @PostMapping
    public ResponseEntity<Cursos> agregarCursos(@Valid @RequestBody Cursos cursos) {
        try {
            Cursos nuevoRegistro = cursosService.guardarCursos(cursos);
            return new ResponseEntity<>(nuevoRegistro, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCursos(@PathVariable Integer id) {
        String resultado = cursosService.eliminar(id);
        if (resultado.contains("correctamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }  
    // Lo deje como estaba ya que un patch aqui no haria mucho sentido, ya que 
    // si se cambia el piloto o el curso se esta creando una relacion diferente
    @PatchMapping("/{id}")
    public ResponseEntity<Cursos> editarCursos(@PathVariable Integer id, @Valid @RequestBody Cursos cursos) {
        try {
            cursos.setID_cursos(id);
            Cursos cursosEditado = cursosService.guardarCursos(cursos);
            return new ResponseEntity<>(cursosEditado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cursos> actualizarCursos(@PathVariable Integer id, @Valid @RequestBody Cursos cursos) {
        try {
            Cursos cursosActualizado = cursosService.actualizarCursos(id, cursos);
            return new ResponseEntity<>(cursosActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }    

    

}
