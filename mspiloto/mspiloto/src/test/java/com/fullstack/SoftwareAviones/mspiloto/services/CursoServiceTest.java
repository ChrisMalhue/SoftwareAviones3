package com.fullstack.SoftwareAviones.mspiloto.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import net.datafaker.Faker;

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

    private final Faker faker = new Faker();

    // el creador

    private Curso createCurso() {
        Curso curso = new Curso();
        curso.setID_curso(1);
        curso.setNombre_curso(faker.regexify("[A-Za-z ]{5,15}"));
        curso.setPilotosCurso(List.of());
        return curso;
    }

    // aca parten los test

    @Test
    public void testObtenerTodos() {
        Curso curso = createCurso();
        when(cursoRepository.findAll()).thenReturn(List.of(curso));

        var resultado = cursoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(curso.getNombre_curso(), resultado.get(0).getNombre_curso());
    }

    @Test
    public void testBuscarPorId() {
        Curso curso = createCurso();
        when(cursoRepository.findById(1)).thenReturn(Optional.of(curso));

        var resultado = cursoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(curso.getNombre_curso(), resultado.getNombre_curso());
        verify(cursoRepository, times(1)).findById(1);
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
        assertEquals(curso.getNombre_curso(), resultado.getNombre_curso());
        verify(cursoRepository, times(1)).save(curso);
    }

    @Test
    public void testActualizarCurso() {
        Curso existente = createCurso();
        String nuevoNombre = faker.regexify("[A-Za-z ]{5,15}");
        existente.setNombre_curso(nuevoNombre); // el service muta existente antes del save

        Curso actualizado = new Curso();
        actualizado.setNombre_curso(nuevoNombre);
        actualizado.setPilotosCurso(List.of());

        when(cursoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(cursoRepository.save(any(Curso.class))).thenReturn(existente);

        var resultado = cursoService.actualizarCurso(1, actualizado);

        assertNotNull(resultado);
        assertEquals(nuevoNombre, resultado.getNombre_curso());
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    public void testActualizarCursoNoExiste() {
        when(cursoRepository.findById(99)).thenReturn(Optional.empty());
        Curso actualizado = new Curso();
        actualizado.setNombre_curso(faker.regexify("[A-Za-z ]{5,15}"));

        assertThrows(RuntimeException.class, () -> cursoService.actualizarCurso(99, actualizado));
    }

    @Test
    public void testPatchCurso() {
        Curso existente = createCurso();
        String nuevoNombre = faker.regexify("[A-Za-z ]{5,15}");
        existente.setNombre_curso(nuevoNombre); // patch muta existente antes del save

        Curso patch = new Curso();
        patch.setNombre_curso(nuevoNombre);

        when(cursoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(cursoRepository.save(any(Curso.class))).thenReturn(existente);

        var resultado = cursoService.patchCurso(1, patch);

        assertNotNull(resultado);
        assertEquals(nuevoNombre, resultado.getNombre_curso());
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    public void testPatchCursoNoExiste() {
        when(cursoRepository.findById(99)).thenReturn(Optional.empty());
        Curso patch = new Curso();
        patch.setNombre_curso(faker.regexify("[A-Za-z ]{5,15}"));

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