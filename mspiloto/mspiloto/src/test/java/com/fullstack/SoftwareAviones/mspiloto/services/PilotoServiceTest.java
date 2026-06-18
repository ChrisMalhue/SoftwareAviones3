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

import com.fullstack.SoftwareAviones.mspiloto.model.Piloto;
import com.fullstack.SoftwareAviones.mspiloto.repository.PilotoRepository;

@SpringBootTest
public class PilotoServiceTest {
    
    @Autowired
    private PilotoService pilotoService;

    @MockitoBean
    private PilotoRepository pilotoRepository;

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

    @Test
    public void testObtenerTodos() {
        when(pilotoRepository.findAll()).thenReturn(List.of(createPiloto()));
        var resultado = pilotoService.obtenerTodos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    @Test
    public void testBuscarPorId() {
        when(pilotoRepository.findById(1)).thenReturn(Optional.of(createPiloto()));
        var resultado = pilotoService.buscarPorId(1);
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(pilotoRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> pilotoService.buscarPorId(99));
    }

    @Test
    public void testGuardarPiloto() {
        Piloto piloto = createPiloto();
        when(pilotoRepository.save(piloto)).thenReturn(piloto);
        var resultado = pilotoService.guardarPiloto(piloto);
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    public void testActualizarPiloto() {
        Piloto existente = createPiloto();
        Piloto actualizado = createPiloto();
        actualizado.setNombre("Carlos");

        when(pilotoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(pilotoRepository.save(any(Piloto.class))).thenReturn(actualizado);

        var resultado = pilotoService.actualizarPiloto(1, actualizado);
        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNombre());
    }

    @Test
    public void testActualizarPilotoNoExiste() {
        when(pilotoRepository.findById(99)).thenReturn(Optional.empty());
        Piloto actualizado = createPiloto();
        assertThrows(RuntimeException.class, () -> pilotoService.actualizarPiloto(99, actualizado));
    }

    @Test
    public void testPatchPiloto() {
        Piloto existente = createPiloto();
        Piloto patch = new Piloto();
        patch.setNombre("Carlos");

        when(pilotoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(pilotoRepository.save(any(Piloto.class))).thenReturn(existente);

        var resultado = pilotoService.patchPiloto(1, patch);
        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNombre());
    }

    @Test
    public void testPatchPilotoNoExiste() {
        when(pilotoRepository.findById(99)).thenReturn(Optional.empty());
        Piloto patch = new Piloto();
        patch.setNombre("Carlos");
        assertThrows(RuntimeException.class, () -> pilotoService.patchPiloto(99, patch));
    }

    @Test
    public void testEliminar() {
        Piloto piloto = createPiloto();
        when(pilotoRepository.findById(1)).thenReturn(Optional.of(piloto));
        doNothing().when(pilotoRepository).delete(piloto);

        String resultado = pilotoService.eliminar(1);
        assertTrue(resultado.contains("exitosamente"));
        verify(pilotoRepository, times(1)).delete(piloto);
    }

    @Test
    public void testEliminarNoExiste() {
        when(pilotoRepository.findById(99)).thenReturn(Optional.empty());
        String resultado = pilotoService.eliminar(99);
        assertTrue(resultado.contains("no existe"));
    }

    @Test
    public void testBuscarPorNombre() {
        when(pilotoRepository.buscarPorNombre("Juan")).thenReturn(List.of(createPiloto()));
        var resultado = pilotoService.buscarPorNombre("Juan");
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    @Test
    public void testBuscarPorNombreNoExiste() {
        when(pilotoRepository.buscarPorNombre("Inexistente")).thenReturn(List.of());
        var resultado = pilotoService.buscarPorNombre("Inexistente");
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPilotosConHorasMinimas() {
        when(pilotoRepository.buscarPilotosConHorasMinimas(100)).thenReturn(List.of(createPiloto()));
        var resultado = pilotoService.buscarPilotosConHorasMinimas(100);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(500, resultado.get(0).getHoras_vuelo());
    }

    @Test
    public void testBuscarPilotosConHorasMinimasNoExiste() {
        when(pilotoRepository.buscarPilotosConHorasMinimas(9999)).thenReturn(List.of());
        var resultado = pilotoService.buscarPilotosConHorasMinimas(9999);
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
