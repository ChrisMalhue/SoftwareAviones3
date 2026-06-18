package com.fullstack.SoftwareAviones.msavion.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

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

    private Fabricante createFabricante() {
        Fabricante fabricante = new Fabricante();
        fabricante.setId_fabricante(1);
        fabricante.setNombre_fabricante("Boeing");
        fabricante.setAviones(List.of());
        return fabricante;
    }
    
    @Test
    public void testObtenerTodos() {
        when(fabricanteRepository.findAll()).thenReturn(List.of(createFabricante()));
        var resultado = fabricanteService.obtenerTodos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Boeing", resultado.get(0).getNombre_fabricante());
    }
    
    @Test
    public void testBuscarPorId() {
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(createFabricante()));
        var resultado = fabricanteService.buscarPorId(1);
        assertNotNull(resultado);
        assertEquals("Boeing", resultado.getNombre_fabricante());
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
        assertEquals("Boeing", resultado.getNombre_fabricante());
    }
    
    @Test
    public void testActualizarFabricante() {
        Fabricante existente = createFabricante();
        Fabricante actualizado = new Fabricante();
        actualizado.setNombre_fabricante("Airbus");
        actualizado.setAviones(List.of());

        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(existente));
        when(fabricanteRepository.save(any(Fabricante.class))).thenReturn(existente);

        var resultado = fabricanteService.actualizarFabricante(1, actualizado);
        assertNotNull(resultado);
        assertEquals("Airbus", resultado.getNombre_fabricante());
    }

    @Test
    public void testActualizarFabricanteNoExiste() {
        when(fabricanteRepository.findById(99)).thenReturn(Optional.empty());
        Fabricante actualizado = new Fabricante();
        actualizado.setNombre_fabricante("Airbus");
        actualizado.setAviones(List.of());
        assertThrows(RuntimeException.class, () -> fabricanteService.actualizarFabricante(99, actualizado));
    }

    @Test
    public void testPatchFabricante() {
        Fabricante existente = createFabricante();
        Fabricante patchData = new Fabricante();
        patchData.setNombre_fabricante("Airbus");
        patchData.setAviones(List.of());

        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(existente));
        when(fabricanteRepository.save(any(Fabricante.class))).thenReturn(existente);

        var resultado = fabricanteService.patchFabricante(1, patchData);
        assertNotNull(resultado);
        assertEquals("Airbus", resultado.getNombre_fabricante());
    }

    @Test
    public void testPatchFabricanteNoExiste() {
        when(fabricanteRepository.findById(99)).thenReturn(Optional.empty());
        Fabricante patchData = new Fabricante();
        patchData.setNombre_fabricante("Airbus");
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
