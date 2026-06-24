package com.fullstack.SoftwareAviones.msubicacion.services;

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

import com.fullstack.SoftwareAviones.msubicacion.model.Region;
import com.fullstack.SoftwareAviones.msubicacion.repository.RegionRepository;

@SpringBootTest
public class RegionServiceTest {

    @Autowired
    private RegionService regionService;

    @MockitoBean
    private RegionRepository regionRepository;

    private final Faker faker = new Faker();

    // el creador

    private Region createRegion() {
        Region region = new Region();
        region.setID_region(1);
        region.setRegion(faker.regexify("[A-Za-z]{5,10}"));
        region.setComunas(List.of());
        return region;
    }

    // aca parten los test

    @Test
    public void testObtenerTodos() {
        Region region = createRegion();
        when(regionRepository.findAll()).thenReturn(List.of(region));

        var resultado = regionService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(region.getRegion(), resultado.get(0).getRegion());
    }

    @Test
    public void testBuscarPorId() {
        Region region = createRegion();
        when(regionRepository.findById(1)).thenReturn(Optional.of(region));

        var resultado = regionService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(region.getRegion(), resultado.getRegion());
        verify(regionRepository, times(1)).findById(1);
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
        assertEquals(region.getRegion(), resultado.getRegion());
        verify(regionRepository, times(1)).save(region);
    }

    @Test
    public void testActualizarRegion() {
        Region existente = createRegion();
        String nuevaRegion = faker.regexify("[A-Za-z]{5,10}");
        existente.setRegion(nuevaRegion); // el service muta existente antes del save

        Region actualizado = new Region();
        actualizado.setRegion(nuevaRegion);
        actualizado.setComunas(List.of());

        when(regionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(regionRepository.save(any(Region.class))).thenReturn(existente);

        var resultado = regionService.actualizarRegion(1, actualizado);

        assertNotNull(resultado);
        assertEquals(nuevaRegion, resultado.getRegion());
        verify(regionRepository, times(1)).save(any(Region.class));
    }

    @Test
    public void testActualizarRegionNoExiste() {
        when(regionRepository.findById(99)).thenReturn(Optional.empty());
        Region actualizado = new Region();
        actualizado.setRegion(faker.regexify("[A-Za-z]{5,10}"));

        assertThrows(RuntimeException.class, () -> regionService.actualizarRegion(99, actualizado));
    }

    @Test
    public void testPatchRegion() {
        Region existente = createRegion();
        String nuevaRegion = faker.regexify("[A-Za-z]{5,10}");
        existente.setRegion(nuevaRegion); // patch muta existente antes del save

        Region patch = new Region();
        patch.setRegion(nuevaRegion);

        when(regionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(regionRepository.save(any(Region.class))).thenReturn(existente);

        var resultado = regionService.patchRegion(1, patch);

        assertNotNull(resultado);
        assertEquals(nuevaRegion, resultado.getRegion());
        verify(regionRepository, times(1)).save(any(Region.class));
    }

    @Test
    public void testPatchRegionNoExiste() {
        when(regionRepository.findById(99)).thenReturn(Optional.empty());
        Region patch = new Region();
        patch.setRegion(faker.regexify("[A-Za-z]{5,10}"));

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