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

    private final Faker faker = new Faker();

    // los creadores

    private Region createRegion() {
        Region region = new Region();
        region.setID_region(1);
        region.setRegion(faker.regexify("[A-Za-z]{5,10}"));
        region.setComunas(List.of());
        return region;
    }

    private Comuna createComuna(Region region) {
        Comuna comuna = new Comuna();
        comuna.setID_comuna(1);
        comuna.setComuna(faker.regexify("[A-Za-z]{5,10}"));
        comuna.setRegion(region);
        comuna.setAerodromos(List.of());
        return comuna;
    }

    // aca parten los test

    @Test
    public void testObtenerTodos() {
        Region region = createRegion();
        Comuna comuna = createComuna(region);
        when(comunaRepository.findAll()).thenReturn(List.of(comuna));

        var resultado = comunaService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(comuna.getComuna(), resultado.get(0).getComuna());
    }

    @Test
    public void testBuscarPorId() {
        Region region = createRegion();
        Comuna comuna = createComuna(region);
        when(comunaRepository.findById(1)).thenReturn(Optional.of(comuna));

        var resultado = comunaService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(comuna.getComuna(), resultado.getComuna());
        verify(comunaRepository, times(1)).findById(1);
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(comunaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> comunaService.buscarPorId(99));
    }

    @Test
    public void testGuardarComuna() {
        Region region = createRegion();
        Comuna comuna = createComuna(region);
        when(regionRepository.findById(1)).thenReturn(Optional.of(region));
        when(comunaRepository.save(any(Comuna.class))).thenReturn(comuna);

        var resultado = comunaService.guardarComuna(comuna);

        assertNotNull(resultado);
        assertEquals(comuna.getComuna(), resultado.getComuna());
        verify(comunaRepository, times(1)).save(any(Comuna.class));
    }

    @Test
    public void testGuardarComunaRegionNoExiste() {
        Region region = createRegion();
        Comuna comuna = createComuna(region);
        when(regionRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> comunaService.guardarComuna(comuna));
    }

    @Test
    public void testActualizarComuna() {
        Region region = createRegion();
        Comuna existente = createComuna(region);
        String nuevaComuna = faker.regexify("[A-Za-z]{5,10}");
        existente.setComuna(nuevaComuna); // el service muta existente antes del save

        Comuna actualizado = new Comuna();
        actualizado.setComuna(nuevaComuna);
        actualizado.setRegion(region);
        actualizado.setAerodromos(List.of());

        when(comunaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(regionRepository.findById(1)).thenReturn(Optional.of(region));
        when(comunaRepository.save(any(Comuna.class))).thenReturn(existente);

        var resultado = comunaService.actualizarComuna(1, actualizado);

        assertNotNull(resultado);
        assertEquals(nuevaComuna, resultado.getComuna());
        verify(comunaRepository, times(1)).save(any(Comuna.class));
    }

    @Test
    public void testActualizarComunaNoExiste() {
        when(comunaRepository.findById(99)).thenReturn(Optional.empty());
        Region region = createRegion();
        Comuna actualizado = new Comuna();
        actualizado.setComuna(faker.regexify("[A-Za-z]{5,10}"));
        actualizado.setRegion(region);

        assertThrows(RuntimeException.class, () -> comunaService.actualizarComuna(99, actualizado));
    }

    @Test
    public void testPatchComuna() {
        Region region = createRegion();
        Comuna existente = createComuna(region);
        String nuevaComuna = faker.regexify("[A-Za-z]{5,10}");
        existente.setComuna(nuevaComuna); // patch muta existente antes del save

        Comuna patch = new Comuna();
        patch.setComuna(nuevaComuna);

        when(comunaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(comunaRepository.save(any(Comuna.class))).thenReturn(existente);

        var resultado = comunaService.patchComuna(1, patch);

        assertNotNull(resultado);
        assertEquals(nuevaComuna, resultado.getComuna());
        verify(comunaRepository, times(1)).save(any(Comuna.class));
    }

    @Test
    public void testPatchComunaNoExiste() {
        when(comunaRepository.findById(99)).thenReturn(Optional.empty());
        Comuna patch = new Comuna();
        patch.setComuna(faker.regexify("[A-Za-z]{5,10}"));

        assertThrows(RuntimeException.class, () -> comunaService.patchComuna(99, patch));
    }

    @Test
    public void testEliminar() {
        Region region = createRegion();
        Comuna comuna = createComuna(region);
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