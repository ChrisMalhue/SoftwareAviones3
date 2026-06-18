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

import com.fullstack.SoftwareAviones.msubicacion.model.Comuna;
import com.fullstack.SoftwareAviones.msubicacion.model.Region;
import com.fullstack.SoftwareAviones.msubicacion.repository.ComunaRepository;
import com.fullstack.SoftwareAviones.msubicacion.repository.RegionRepository;

@SpringBootTest
public class ComunaServiceTest {

    @Autowired
    private ComunaService comunaService;

    @MockitoBean
    private ComunaRepository comunaRepository;

    @MockitoBean
    private RegionRepository regionRepository;

    private Region createRegion() {
        Region region = new Region();
        region.setID_region(1);
        region.setRegion("Metropolitana");
        region.setComunas(List.of());
        return region;
    }

    private Comuna createComuna() {
        Comuna comuna = new Comuna();
        comuna.setID_comuna(1);
        comuna.setComuna("Santiago");
        comuna.setRegion(createRegion());
        comuna.setAerodromos(List.of());
        return comuna;
    }

    @Test
    public void testObtenerTodos() {
        when(comunaRepository.findAll()).thenReturn(List.of(createComuna()));
        var resultado = comunaService.obtenerTodos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Santiago", resultado.get(0).getComuna());
    }

    @Test
    public void testBuscarPorId() {
        when(comunaRepository.findById(1)).thenReturn(Optional.of(createComuna()));
        var resultado = comunaService.buscarPorId(1);
        assertNotNull(resultado);
        assertEquals("Santiago", resultado.getComuna());
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(comunaRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> comunaService.buscarPorId(99));
    }

    @Test
    public void testGuardarComuna() {
        Comuna comuna = createComuna();
        when(regionRepository.findById(1)).thenReturn(Optional.of(createRegion()));
        when(comunaRepository.save(any(Comuna.class))).thenReturn(comuna);
        var resultado = comunaService.guardarComuna(comuna);
        assertNotNull(resultado);
        assertEquals("Santiago", resultado.getComuna());
    }

    @Test
    public void testGuardarComunaRegionNoExiste() {
        Comuna comuna = createComuna();
        when(regionRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> comunaService.guardarComuna(comuna));
    }

    @Test
    public void testActualizarComuna() {
        Comuna existente = createComuna();
        Comuna actualizado = new Comuna();
        actualizado.setComuna("Providencia");
        actualizado.setRegion(createRegion());
        actualizado.setAerodromos(List.of());

        when(comunaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(regionRepository.findById(1)).thenReturn(Optional.of(createRegion()));
        when(comunaRepository.save(any(Comuna.class))).thenReturn(existente);

        var resultado = comunaService.actualizarComuna(1, actualizado);
        assertNotNull(resultado);
        assertEquals("Providencia", resultado.getComuna());
    }

    @Test
    public void testActualizarComunaNoExiste() {
        when(comunaRepository.findById(99)).thenReturn(Optional.empty());
        Comuna actualizado = new Comuna();
        actualizado.setComuna("Providencia");
        actualizado.setRegion(createRegion());
        assertThrows(RuntimeException.class, () -> comunaService.actualizarComuna(99, actualizado));
    }

    @Test
    public void testPatchComuna() {
        Comuna existente = createComuna();
        Comuna patch = new Comuna();
        patch.setComuna("Providencia");

        when(comunaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(comunaRepository.save(any(Comuna.class))).thenReturn(existente);

        var resultado = comunaService.patchComuna(1, patch);
        assertNotNull(resultado);
        assertEquals("Providencia", resultado.getComuna());
    }

    @Test
    public void testPatchComunaNoExiste() {
        when(comunaRepository.findById(99)).thenReturn(Optional.empty());
        Comuna patch = new Comuna();
        patch.setComuna("Providencia");
        assertThrows(RuntimeException.class, () -> comunaService.patchComuna(99, patch));
    }

    @Test
    public void testEliminar() {
        Comuna comuna = createComuna();
        when(comunaRepository.findById(1)).thenReturn(Optional.of(comuna));
        doNothing().when(comunaRepository).delete(comuna);

        String resultado = comunaService.eliminar(1);
        assertTrue(resultado.contains("exitosamente"));
        verify(comunaRepository, times(1)).delete(comuna);
    }

    @Test
    public void testEliminarNoExiste() {
        when(comunaRepository.findById(99)).thenReturn(Optional.empty());
        String resultado = comunaService.eliminar(99);
        assertTrue(resultado.contains("no existe"));
    }
}
