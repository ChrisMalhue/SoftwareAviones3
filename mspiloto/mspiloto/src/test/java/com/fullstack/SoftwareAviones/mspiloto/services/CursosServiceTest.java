package com.fullstack.SoftwareAviones.mspiloto.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import net.datafaker.Faker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.fullstack.SoftwareAviones.mspiloto.model.Curso;
import com.fullstack.SoftwareAviones.mspiloto.model.Cursos;
import com.fullstack.SoftwareAviones.mspiloto.model.Piloto;
import com.fullstack.SoftwareAviones.mspiloto.repository.CursoRepository;
import com.fullstack.SoftwareAviones.mspiloto.repository.CursosRepository;
import com.fullstack.SoftwareAviones.mspiloto.repository.PilotoRepository;

@SpringBootTest
public class CursosServiceTest {

    @Autowired
    private CursosService cursosService;

    @MockitoBean
    private CursosRepository cursosRepository;

    @MockitoBean
    private PilotoRepository pilotoRepository;

    @MockitoBean
    private CursoRepository cursoRepository;

    private final Faker faker = new Faker();

    // los creadores

    private Piloto createPiloto() {
        Piloto piloto = new Piloto();
        piloto.setID_piloto(1);
        piloto.setRut(faker.regexify("[0-9]{2}\\.[0-9]{3}\\.[0-9]{3}-[0-9]"));
        piloto.setNombre(faker.regexify("[A-Za-z]{3,10}"));
        piloto.setApellido(faker.regexify("[A-Za-z]{3,10}"));
        piloto.setFecha_nacimiento(new Date(90, 1, 1));
        piloto.setHoras_vuelo(faker.number().numberBetween(0, 5000));
        piloto.setCursosAprendidos(List.of());
        return piloto;
    }

    private Curso createCurso() {
        Curso curso = new Curso();
        curso.setID_curso(1);
        curso.setNombre_curso(faker.regexify("[A-Za-z ]{5,15}"));
        curso.setPilotosCurso(List.of());
        return curso;
    }

    private Cursos createCursos(Piloto piloto, Curso curso) {
        Cursos cursos = new Cursos();
        cursos.setID_cursos(1);
        cursos.setPiloto(piloto);
        cursos.setCurso(curso);
        return cursos;
    }

    // aca parten los test

    @Test
    public void testObtenerTodos() {
        Piloto piloto = createPiloto();
        Curso curso = createCurso();
        Cursos cursos = createCursos(piloto, curso);
        when(cursosRepository.findAll()).thenReturn(List.of(cursos));

        var resultado = cursosService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(piloto.getNombre(), resultado.get(0).getPiloto());
    }

    @Test
    public void testBuscarPorId() {
        Piloto piloto = createPiloto();
        Curso curso = createCurso();
        Cursos cursos = createCursos(piloto, curso);
        when(cursosRepository.findById(1)).thenReturn(Optional.of(cursos));

        var resultado = cursosService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(piloto.getNombre(), resultado.getPiloto());
        verify(cursosRepository, times(1)).findById(1);
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(cursosRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cursosService.buscarPorId(99));
    }

    @Test
    public void testGuardarCursos() {
        Piloto piloto = createPiloto();
        Curso curso = createCurso();
        Cursos cursos = createCursos(piloto, curso);

        when(pilotoRepository.findById(1)).thenReturn(Optional.of(piloto));
        when(cursoRepository.findById(1)).thenReturn(Optional.of(curso));
        when(cursosRepository.save(any(Cursos.class))).thenReturn(cursos);

        var resultado = cursosService.guardarCursos(cursos);

        assertNotNull(resultado);
        assertEquals(piloto.getNombre(), resultado.getPiloto());
        verify(cursosRepository, times(1)).save(any(Cursos.class));
    }

    @Test
    public void testGuardarCursosPilotoNoExiste() {
        Piloto piloto = createPiloto();
        Curso curso = createCurso();
        Cursos cursos = createCursos(piloto, curso);
        when(pilotoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cursosService.guardarCursos(cursos));
    }

    @Test
    public void testGuardarCursosCursoNoExiste() {
        Piloto piloto = createPiloto();
        Curso curso = createCurso();
        Cursos cursos = createCursos(piloto, curso);
        when(pilotoRepository.findById(1)).thenReturn(Optional.of(piloto));
        when(cursoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cursosService.guardarCursos(cursos));
    }

    @Test
    public void testActualizarCursos() {
        Piloto piloto = createPiloto();
        Curso curso = createCurso();
        Cursos existente = createCursos(piloto, curso);
        Cursos actualizado = createCursos(piloto, curso);

        when(cursosRepository.findById(1)).thenReturn(Optional.of(existente));
        when(pilotoRepository.findById(1)).thenReturn(Optional.of(piloto));
        when(cursoRepository.findById(1)).thenReturn(Optional.of(curso));
        when(cursosRepository.save(any(Cursos.class))).thenReturn(actualizado);

        var resultado = cursosService.actualizarCursos(1, actualizado);

        assertNotNull(resultado);
        assertEquals(piloto.getNombre(), resultado.getPiloto());
        verify(cursosRepository, times(1)).save(any(Cursos.class));
    }

    @Test
    public void testActualizarCursosNoExiste() {
        when(cursosRepository.findById(99)).thenReturn(Optional.empty());
        Cursos actualizado = createCursos(createPiloto(), createCurso());

        assertThrows(RuntimeException.class, () -> cursosService.actualizarCursos(99, actualizado));
    }

    @Test
    public void testEliminar() {
        Piloto piloto = createPiloto();
        Curso curso = createCurso();
        Cursos cursos = createCursos(piloto, curso);
        when(cursosRepository.findById(1)).thenReturn(Optional.of(cursos));
        doNothing().when(cursosRepository).delete(cursos);

        String resultado = cursosService.eliminar(1);

        assertTrue(resultado.contains("correctamente"));
        verify(cursosRepository, times(1)).delete(cursos);
    }

    @Test
    public void testEliminarNoExiste() {
        when(cursosRepository.findById(99)).thenReturn(Optional.empty());

        String resultado = cursosService.eliminar(99);

        assertTrue(resultado.contains("no existe"));
    }
}