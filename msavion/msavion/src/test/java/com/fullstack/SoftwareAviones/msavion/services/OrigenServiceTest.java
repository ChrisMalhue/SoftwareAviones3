package com.fullstack.SoftwareAviones.msavion.services;

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

import com.fullstack.SoftwareAviones.msavion.model.Origen;
import com.fullstack.SoftwareAviones.msavion.repository.OrigenRepository;

@SpringBootTest
public class OrigenServiceTest {

    @Autowired
    private OrigenService origenService;

    @MockitoBean
    private OrigenRepository origenRepository;

    private final Faker faker = new Faker();

    // el creador

    private Origen createOrigen() {
        Origen origen = new Origen();
        origen.setId_origen(1);
        origen.setPais_origen(faker.regexify("[A-Za-z]{3,10}"));
        origen.setAviones(List.of());
        return origen;
    }

    // aca parten los test

    @Test
    public void testObtenerTodos() {
        Origen origen = createOrigen();
        when(origenRepository.findAll()).thenReturn(List.of(origen));

        var resultado = origenService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(origen.getPais_origen(), resultado.get(0).getPais_origen());
    }

    @Test
    public void testBuscarPorId() {
        Origen origen = createOrigen();
        when(origenRepository.findById(1)).thenReturn(Optional.of(origen));

        var resultado = origenService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(origen.getPais_origen(), resultado.getPais_origen());
        verify(origenRepository, times(1)).findById(1);
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(origenRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> origenService.buscarPorId(99));
    }

    @Test
    public void testGuardarOrigen() {
        Origen origen = createOrigen();
        when(origenRepository.save(origen)).thenReturn(origen);

        var resultado = origenService.guardarOrigen(origen);

        assertNotNull(resultado);
        assertEquals(origen.getPais_origen(), resultado.getPais_origen());
        verify(origenRepository, times(1)).save(origen);
    }

    @Test
    public void testActualizarOrigen() {
        Origen existente = createOrigen();
        String nuevoPais = faker.regexify("[A-Za-z]{3,10}");
        existente.setPais_origen(nuevoPais); // el service muta existente antes del save

        Origen actualizado = new Origen();
        actualizado.setPais_origen(nuevoPais);
        actualizado.setAviones(List.of());

        when(origenRepository.findById(1)).thenReturn(Optional.of(existente));
        when(origenRepository.save(any(Origen.class))).thenReturn(existente);

        var resultado = origenService.actualizarOrigen(1, actualizado);

        assertNotNull(resultado);
        assertEquals(nuevoPais, resultado.getPais_origen());
        verify(origenRepository, times(1)).save(any(Origen.class));
    }

    @Test
    public void testActualizarOrigenNoExiste() {
        when(origenRepository.findById(99)).thenReturn(Optional.empty());
        Origen actualizado = new Origen();
        actualizado.setPais_origen(faker.regexify("[A-Za-z]{3,10}"));
        actualizado.setAviones(List.of());

        assertThrows(RuntimeException.class, () -> origenService.actualizarOrigen(99, actualizado));
    }

    @Test
    public void testPatchOrigen() {
        Origen existente = createOrigen();
        String nuevoPais = faker.regexify("[A-Za-z]{3,10}");
        existente.setPais_origen(nuevoPais); // patch muta existente antes del save

        Origen patchData = new Origen();
        patchData.setPais_origen(nuevoPais);
        patchData.setAviones(List.of());

        when(origenRepository.findById(1)).thenReturn(Optional.of(existente));
        when(origenRepository.save(any(Origen.class))).thenReturn(existente);

        var resultado = origenService.patchOrigen(1, patchData);

        assertNotNull(resultado);
        assertEquals(nuevoPais, resultado.getPais_origen());
        verify(origenRepository, times(1)).save(any(Origen.class));
    }

    @Test
    public void testPatchOrigenNoExiste() {
        when(origenRepository.findById(99)).thenReturn(Optional.empty());
        Origen patchData = new Origen();
        patchData.setPais_origen(faker.regexify("[A-Za-z]{3,10}"));

        assertThrows(RuntimeException.class, () -> origenService.patchOrigen(99, patchData));
    }

    @Test
    public void testEliminar() {
        Origen origen = createOrigen();
        when(origenRepository.findById(1)).thenReturn(Optional.of(origen));
        doNothing().when(origenRepository).delete(origen);

        String resultado = origenService.eliminar(1);

        assertTrue(resultado.contains("correctamente") || resultado.contains("exitosamente"));
        verify(origenRepository, times(1)).delete(origen);
    }

    @Test
    public void testEliminarNoExiste() {
        when(origenRepository.findById(99)).thenReturn(Optional.empty());

        String resultado = origenService.eliminar(99);

        assertTrue(resultado.contains("no existe"));
    }
}