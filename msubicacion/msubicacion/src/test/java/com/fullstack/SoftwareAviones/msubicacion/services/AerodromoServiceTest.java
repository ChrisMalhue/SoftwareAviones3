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

import com.fullstack.SoftwareAviones.msubicacion.model.Aerodromo;
import com.fullstack.SoftwareAviones.msubicacion.model.Comuna;
import com.fullstack.SoftwareAviones.msubicacion.model.Region;
import com.fullstack.SoftwareAviones.msubicacion.repository.AerodromoRepository;
import com.fullstack.SoftwareAviones.msubicacion.repository.ComunaRepository;

@SpringBootTest
public class AerodromoServiceTest {

    @Autowired
    private AerodromoService aerodromoService;

    @MockitoBean
    private AerodromoRepository aerodromoRepository;

    @MockitoBean
    private ComunaRepository comunaRepository;
    
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

    private Aerodromo createAerodromo() {
        Aerodromo aerodromo = new Aerodromo();
        aerodromo.setID_aerodromo(1);
        aerodromo.setNombre_aerodromo("Aerodromo El Bosque");
        aerodromo.setComuna(createComuna());
        return aerodromo;
    }

    @Test
    public void testObtenerTodos() {
        when(aerodromoRepository.findAll()).thenReturn(List.of(createAerodromo()));
        var resultado = aerodromoService.obtenerTodos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Aerodromo El Bosque", resultado.get(0).getNombre_aerodromo());
    }

    @Test
    public void testBuscarPorId() {
        when(aerodromoRepository.findById(1)).thenReturn(Optional.of(createAerodromo()));
        var resultado = aerodromoService.buscarPorId(1);
        assertNotNull(resultado);
        assertEquals("Aerodromo El Bosque", resultado.getNombre_aerodromo());
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(aerodromoRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> aerodromoService.buscarPorId(99));
    }

    @Test
    public void testGuardarAerodromo() {
        Aerodromo aerodromo = createAerodromo();
        when(comunaRepository.findById(1)).thenReturn(Optional.of(createComuna()));
        when(aerodromoRepository.save(any(Aerodromo.class))).thenReturn(aerodromo);
        var resultado = aerodromoService.guardarAerodromo(aerodromo);
        assertNotNull(resultado);
        assertEquals("Aerodromo El Bosque", resultado.getNombre_aerodromo());
    }

    @Test
    public void testGuardarAerodromoComunaNoExiste() {
        Aerodromo aerodromo = createAerodromo();
        when(comunaRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> aerodromoService.guardarAerodromo(aerodromo));
    }

    @Test
    public void testActualizarAerodromo() {
        Aerodromo existente = createAerodromo();
        Aerodromo actualizado = createAerodromo();
        actualizado.setNombre_aerodromo("Aerodromo Tobalaba");

        when(aerodromoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(comunaRepository.findById(1)).thenReturn(Optional.of(createComuna()));
        when(aerodromoRepository.save(any(Aerodromo.class))).thenReturn(actualizado);

        var resultado = aerodromoService.actualizarAerodromo(1, actualizado);
        assertNotNull(resultado);
        assertEquals("Aerodromo Tobalaba", resultado.getNombre_aerodromo());
    }

    @Test
    public void testActualizarAerodromoNoExiste() {
        when(aerodromoRepository.findById(99)).thenReturn(Optional.empty());
        Aerodromo actualizado = createAerodromo();
        assertThrows(RuntimeException.class, () -> aerodromoService.actualizarAerodromo(99, actualizado));
    }

    @Test
    public void testPatchAerodromo() {
        Aerodromo existente = createAerodromo();
        Aerodromo patch = new Aerodromo();
        patch.setNombre_aerodromo("Aerodromo Tobalaba");

        when(aerodromoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(aerodromoRepository.save(any(Aerodromo.class))).thenReturn(existente);

        var resultado = aerodromoService.patchAerodromo(1, patch);
        assertNotNull(resultado);
        assertEquals("Aerodromo Tobalaba", resultado.getNombre_aerodromo());
    }

    @Test
    public void testPatchAerodromoNoExiste() {
        when(aerodromoRepository.findById(99)).thenReturn(Optional.empty());
        Aerodromo patch = new Aerodromo();
        patch.setNombre_aerodromo("Aerodromo Tobalaba");
        assertThrows(RuntimeException.class, () -> aerodromoService.patchAerodromo(99, patch));
    }

    @Test
    public void testEliminar() {
        Aerodromo aerodromo = createAerodromo();
        when(aerodromoRepository.findById(1)).thenReturn(Optional.of(aerodromo));
        doNothing().when(aerodromoRepository).delete(aerodromo);

        String resultado = aerodromoService.eliminar(1);
        assertTrue(resultado.contains("exitosamente"));
        verify(aerodromoRepository, times(1)).delete(aerodromo);
    }

    @Test
    public void testEliminarNoExiste() {
        when(aerodromoRepository.findById(99)).thenReturn(Optional.empty());
        String resultado = aerodromoService.eliminar(99);
        assertTrue(resultado.contains("no existe"));
    }
}
