package com.fullstack.SoftwareAviones.msubicacion.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.fullstack.SoftwareAviones.msubicacion.model.Region;
import com.fullstack.SoftwareAviones.msubicacion.repository.RegionRepository;

@SpringBootTest
public class RegionServiceTest {

    @Autowired
    private RegionService regionService;

    @MockitoBean
    private RegionRepository regionRepository;

    private Region createRegion() {
        Region region = new Region();
        region.setID_region(1);
        region.setRegion("Metropolitana");
        region.setComunas(List.of());
        return region;
    }

    @Test
    public void testObtenerTodos() {
        when(regionRepository.findAll()).thenReturn(List.of(createRegion()));
        var resultado = regionService.obtenerTodos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Metropolitana", resultado.get(0).getRegion());
    }

    @Test
    public void testBuscarPorId() {
        when(regionRepository.findById(1)).thenReturn(Optional.of(createRegion()));
        var resultado = regionService.buscarPorId(1);
        assertNotNull(resultado);
        assertEquals("Metropolitana", resultado.getRegion());
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(regionRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> regionService.buscarPorId(99));
    }

    @Test
    public void testGuardarRegion() {
        Region region = createRegion();
        when(regionRepository.save(region)).thenReturn(region);
        var resultado = regionService.guardarRegion(region);
        assertNotNull(resultado);
        assertEquals("Metropolitana", resultado.getRegion());
    }

    @Test
    public void testActualizarRegion() {
        Region existente = createRegion();
        Region actualizado = new Region();
        actualizado.setRegion("Valparaiso");
        actualizado.setComunas(List.of());

        when(regionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(regionRepository.save(any(Region.class))).thenReturn(existente);

        var resultado = regionService.actualizarRegion(1, actualizado);
        assertNotNull(resultado);
        assertEquals("Valparaiso", resultado.getRegion());
    }

    @Test
    public void testActualizarRegionNoExiste() {
        when(regionRepository.findById(99)).thenReturn(Optional.empty());
        Region actualizado = new Region();
        actualizado.setRegion("Valparaiso");
        assertThrows(RuntimeException.class, () -> regionService.actualizarRegion(99, actualizado));
    }

    @Test
    public void testPatchRegion() {
        Region existente = createRegion();
        Region patch = new Region();
        patch.setRegion("Valparaiso");

        when(regionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(regionRepository.save(any(Region.class))).thenReturn(existente);

        var resultado = regionService.patchRegion(1, patch);
        assertNotNull(resultado);
        assertEquals("Valparaiso", resultado.getRegion());
    }

    @Test
    public void testPatchRegionNoExiste() {
        when(regionRepository.findById(99)).thenReturn(Optional.empty());
        Region patch = new Region();
        patch.setRegion("Valparaiso");
        assertThrows(RuntimeException.class, () -> regionService.patchRegion(99, patch));
    }

    @Test
    public void testEliminar() {
        Region region = createRegion();
        when(regionRepository.findById(1)).thenReturn(Optional.of(region));
        doNothing().when(regionRepository).delete(region);

        String resultado = regionService.eliminar(1);
        assertTrue(resultado.contains("correctamente"));
        verify(regionRepository, times(1)).delete(region);
    }

    @Test
    public void testEliminarNoExiste() {
        when(regionRepository.findById(99)).thenReturn(Optional.empty());
        String resultado = regionService.eliminar(99);
        assertTrue(resultado.contains("no existe"));
    }
}
