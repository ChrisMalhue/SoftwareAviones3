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

import com.fullstack.SoftwareAviones.mspiloto.DTO.CursoDTO;
import com.fullstack.SoftwareAviones.mspiloto.model.Curso;
import com.fullstack.SoftwareAviones.mspiloto.services.CursoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<CursoDTO>> obtenerTodos() {
        List<CursoDTO> cursos = cursoService.obtenerTodos();
        if (cursos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }    

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> buscarPorId(@PathVariable Integer id) {
        try {
            CursoDTO curso = cursoService.buscarPorId(id);
            return new ResponseEntity<>(curso, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } 
    
    @PostMapping
    public ResponseEntity<Curso> agregarCurso(@Valid @RequestBody Curso curso) {
        try {
            Curso nuevoCurso = cursoService.guardarCurso(curso);
            return new ResponseEntity<>(nuevoCurso, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }  
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCurso(@PathVariable Integer id) {
        String resultado = cursoService.eliminar(id);
        if (resultado.contains("correctamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    } 
    
    @PatchMapping("/{id}")
    public ResponseEntity<Curso> patchCurso(@PathVariable Integer id, @RequestBody Curso curso) {
        try {
            Curso cursoEditado = cursoService.patchCurso(id, curso);
            return new ResponseEntity<>(cursoEditado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } 
    
    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizarCurso(@PathVariable Integer id, @Valid @RequestBody Curso curso) {
        try {
            Curso cursoActualizado = cursoService.actualizarCurso(id, curso);
            return new ResponseEntity<>(cursoActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } 
    
    

}
