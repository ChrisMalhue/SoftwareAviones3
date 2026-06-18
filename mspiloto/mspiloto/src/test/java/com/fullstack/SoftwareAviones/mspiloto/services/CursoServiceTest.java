package com.fullstack.SoftwareAviones.mspiloto.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.fullstack.SoftwareAviones.mspiloto.model.Curso;
import com.fullstack.SoftwareAviones.mspiloto.repository.CursoRepository;

@SpringBootTest
public class CursoServiceTest {

    @Autowired
    private CursoService cursoService;

    @MockitoBean
    private CursoRepository cursoRepository;

    private Curso createCurso() {
        Curso curso = new Curso();
        curso.setID_curso(1);
        curso.setNombre_curso("Vuelo Instrumental");
        curso.setPilotosCurso(List.of());
        return curso;
    }

    @Test
    public void testObtenerTodos() {
        when(cursoRepository.findAll()).thenReturn(List.of(createCurso()));
        var resultado = cursoService.obtenerTodos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Vuelo Instrumental", resultado.get(0).getNombre_curso());
    }

    @Test
    public void testBuscarPorId() {
        when(cursoRepository.findById(1)).thenReturn(Optional.of(createCurso()));
        var resultado = cursoService.buscarPorId(1);
        assertNotNull(resultado);
        assertEquals("Vuelo Instrumental", resultado.getNombre_curso());
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(cursoRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cursoService.buscarPorId(99));
    }

    @Test
    public void testGuardarCurso() {
        Curso curso = createCurso();
        when(cursoRepository.save(curso)).thenReturn(curso);
        var resultado = cursoService.guardarCurso(curso);
        assertNotNull(resultado);
        assertEquals("Vuelo Instrumental", resultado.getNombre_curso());
    }    

    @Test
    public void testActualizarCurso() {
        Curso existente = createCurso();
        Curso actualizado = new Curso();
        actualizado.setNombre_curso("Vuelo Nocturno");
        actualizado.setPilotosCurso(List.of());

        when(cursoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(cursoRepository.save(any(Curso.class))).thenReturn(existente);

        var resultado = cursoService.actualizarCurso(1, actualizado);
        assertNotNull(resultado);
        assertEquals("Vuelo Nocturno", resultado.getNombre_curso());
    }

    @Test
    public void testActualizarCursoNoExiste() {
        when(cursoRepository.findById(99)).thenReturn(Optional.empty());
        Curso actualizado = new Curso();
        actualizado.setNombre_curso("Vuelo Nocturno");
        assertThrows(RuntimeException.class, () -> cursoService.actualizarCurso(99, actualizado));
    }

    @Test
    public void testPatchCurso() {
        Curso existente = createCurso();
        Curso patch = new Curso();
        patch.setNombre_curso("Vuelo Nocturno");

        when(cursoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(cursoRepository.save(any(Curso.class))).thenReturn(existente);

        var resultado = cursoService.patchCurso(1, patch);
        assertNotNull(resultado);
        assertEquals("Vuelo Nocturno", resultado.getNombre_curso());
    }

    @Test
    public void testPatchCursoNoExiste() {
        when(cursoRepository.findById(99)).thenReturn(Optional.empty());
        Curso patch = new Curso();
        patch.setNombre_curso("Vuelo Nocturno");
        assertThrows(RuntimeException.class, () -> cursoService.patchCurso(99, patch));
    }
    
    @Test
    public void testEliminar() {
        Curso curso = createCurso();
        when(cursoRepository.findById(1)).thenReturn(Optional.of(curso));
        doNothing().when(cursoRepository).delete(curso);

        String resultado = cursoService.eliminar(1);
        assertTrue(resultado.contains("correctamente"));
        verify(cursoRepository, times(1)).delete(curso);
    }

    @Test
    public void testEliminarNoExiste() {
        when(cursoRepository.findById(99)).thenReturn(Optional.empty());
        String resultado = cursoService.eliminar(99);
        assertTrue(resultado.contains("no existe"));
    }

    

}
