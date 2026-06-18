package com.fullstack.SoftwareAviones.mspiloto.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    private Piloto createPiloto() {
        Piloto piloto = new Piloto();
        piloto.setID_piloto(1);
        piloto.setRut("12.345.678-9");
        piloto.setNombre("Juan");
        piloto.setApellido("Perez");
        piloto.setFecha_nacimiento(new Date(90, 1, 1));
        piloto.setHoras_vuelo(500);
        piloto.setCursosAprendidos(List.of());
        return piloto;
    }

    private Curso createCurso() {
        Curso curso = new Curso();
        curso.setID_curso(1);
        curso.setNombre_curso("Vuelo Instrumental");
        curso.setPilotosCurso(List.of());
        return curso;
    }
    
    private Cursos createCursos() {
        Cursos cursos = new Cursos();
        cursos.setID_cursos(1);
        cursos.setPiloto(createPiloto());
        cursos.setCurso(createCurso());
        return cursos;
    }

    @Test
    public void testObtenerTodos() {
        when(cursosRepository.findAll()).thenReturn(List.of(createCursos()));
        var resultado = cursosService.obtenerTodos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getPiloto());
    }

    @Test
    public void testBuscarPorId() {
        when(cursosRepository.findById(1)).thenReturn(Optional.of(createCursos()));
        var resultado = cursosService.buscarPorId(1);
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getPiloto());
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(cursosRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cursosService.buscarPorId(99));
    }

    @Test
    public void testGuardarCursos() {
        Cursos cursos = createCursos();
        when(pilotoRepository.findById(1)).thenReturn(Optional.of(createPiloto()));
        when(cursoRepository.findById(1)).thenReturn(Optional.of(createCurso()));
        when(cursosRepository.save(any(Cursos.class))).thenReturn(cursos);

        var resultado = cursosService.guardarCursos(cursos);
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getPiloto());
    }

    @Test
    public void testGuardarCursosPilotoNoExiste() {
        Cursos cursos = createCursos();
        when(pilotoRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cursosService.guardarCursos(cursos));
    }

    @Test
    public void testGuardarCursosCursoNoExiste() {
        Cursos cursos = createCursos();
        when(pilotoRepository.findById(1)).thenReturn(Optional.of(createPiloto()));
        when(cursoRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cursosService.guardarCursos(cursos));
    }

    @Test
    public void testActualizarCursos() {
        Cursos existente = createCursos();
        Cursos actualizado = createCursos();

        when(cursosRepository.findById(1)).thenReturn(Optional.of(existente));
        when(pilotoRepository.findById(1)).thenReturn(Optional.of(createPiloto()));
        when(cursoRepository.findById(1)).thenReturn(Optional.of(createCurso()));
        when(cursosRepository.save(any(Cursos.class))).thenReturn(actualizado);

        var resultado = cursosService.actualizarCursos(1, actualizado);
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getPiloto());
    }

    @Test
    public void testActualizarCursosNoExiste() {
        when(cursosRepository.findById(99)).thenReturn(Optional.empty());
        Cursos actualizado = createCursos();
        assertThrows(RuntimeException.class, () -> cursosService.actualizarCursos(99, actualizado));
    }
    
    @Test
    public void testEliminar() {
        Cursos cursos = createCursos();
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
