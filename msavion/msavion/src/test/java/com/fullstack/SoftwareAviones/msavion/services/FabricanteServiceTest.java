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

import com.fullstack.SoftwareAviones.msavion.model.Fabricante;
import com.fullstack.SoftwareAviones.msavion.repository.FabricanteRepository;

@SpringBootTest
public class FabricanteServiceTest {

    @Autowired
    private FabricanteService fabricanteService;

    @MockitoBean
    private FabricanteRepository fabricanteRepository;

    private final Faker faker = new Faker();

    // el creador

    private Fabricante createFabricante() {
        Fabricante fabricante = new Fabricante();
        fabricante.setId_fabricante(1);
        fabricante.setNombre_fabricante(faker.regexify("[A-Za-z]{5,10}"));
        fabricante.setAviones(List.of());
        return fabricante;
    }

    // aca parten los test

    @Test
    public void testObtenerTodos() {
        Fabricante fabricante = createFabricante();
        when(fabricanteRepository.findAll()).thenReturn(List.of(fabricante));

        var resultado = fabricanteService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(fabricante.getNombre_fabricante(), resultado.get(0).getNombre_fabricante());
    }

    @Test
    public void testBuscarPorId() {
        Fabricante fabricante = createFabricante();
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(fabricante));

        var resultado = fabricanteService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(fabricante.getNombre_fabricante(), resultado.getNombre_fabricante());
        verify(fabricanteRepository, times(1)).findById(1);
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(fabricanteRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> fabricanteService.buscarPorId(99));
    }

    @Test
    public void testGuardarFabricante() {
        Fabricante fabricante = createFabricante();
        when(fabricanteRepository.save(fabricante)).thenReturn(fabricante);

        var resultado = fabricanteService.guardarFabricante(fabricante);

        assertNotNull(resultado);
        assertEquals(fabricante.getNombre_fabricante(), resultado.getNombre_fabricante());
        verify(fabricanteRepository, times(1)).save(fabricante);
    }

    @Test
    public void testActualizarFabricante() {
        Fabricante existente = createFabricante();
        Fabricante actualizado = new Fabricante();
        String nuevoNombre = faker.regexify("[A-Za-z]{5,10}");
        actualizado.setNombre_fabricante(nuevoNombre);
        actualizado.setAviones(List.of());

        existente.setNombre_fabricante(nuevoNombre); // el service muta existente antes del save
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(existente));
        when(fabricanteRepository.save(any(Fabricante.class))).thenReturn(existente);

        var resultado = fabricanteService.actualizarFabricante(1, actualizado);

        assertNotNull(resultado);
        assertEquals(nuevoNombre, resultado.getNombre_fabricante());
        verify(fabricanteRepository, times(1)).save(any(Fabricante.class));
    }

    @Test
    public void testActualizarFabricanteNoExiste() {
        when(fabricanteRepository.findById(99)).thenReturn(Optional.empty());
        Fabricante actualizado = new Fabricante();
        actualizado.setNombre_fabricante(faker.regexify("[A-Za-z]{5,10}"));
        actualizado.setAviones(List.of());

        assertThrows(RuntimeException.class, () -> fabricanteService.actualizarFabricante(99, actualizado));
    }

    @Test
    public void testPatchFabricante() {
        Fabricante existente = createFabricante();
        String nuevoNombre = faker.regexify("[A-Za-z]{5,10}");
        existente.setNombre_fabricante(nuevoNombre); // patch muta existente antes del save

        Fabricante patchData = new Fabricante();
        patchData.setNombre_fabricante(nuevoNombre);
        patchData.setAviones(List.of());

        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(existente));
        when(fabricanteRepository.save(any(Fabricante.class))).thenReturn(existente);

        // WHEN
        var resultado = fabricanteService.patchFabricante(1, patchData);

        // THEN
        assertNotNull(resultado);
        assertEquals(nuevoNombre, resultado.getNombre_fabricante());
        verify(fabricanteRepository, times(1)).save(any(Fabricante.class));
    }

    @Test
    public void testPatchFabricanteNoExiste() {
        when(fabricanteRepository.findById(99)).thenReturn(Optional.empty());
        Fabricante patchData = new Fabricante();
        patchData.setNombre_fabricante(faker.regexify("[A-Za-z]{5,10}"));

        assertThrows(RuntimeException.class, () -> fabricanteService.patchFabricante(99, patchData));
    }

    @Test
    public void testEliminar() {
        Fabricante fabricante = createFabricante();
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(fabricante));
        doNothing().when(fabricanteRepository).delete(fabricante);

        String resultado = fabricanteService.eliminar(1);

        assertTrue(resultado.contains("correctamente"));
        verify(fabricanteRepository, times(1)).delete(fabricante);
    }

    @Test
    public void testEliminarNoExiste() {
        when(fabricanteRepository.findById(99)).thenReturn(Optional.empty());

        String resultado = fabricanteService.eliminar(99);

        assertTrue(resultado.contains("no existe"));
    }
}