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

    private Aerodromo createAerodromo(Comuna comuna) {
        Aerodromo aerodromo = new Aerodromo();
        aerodromo.setID_aerodromo(1);
        aerodromo.setNombre_aerodromo(faker.regexify("[A-Za-z ]{5,15}"));
        aerodromo.setComuna(comuna);
        return aerodromo;
    }

    // aca parten los test

    @Test
    public void testObtenerTodos() {
        Region region = createRegion();
        Comuna comuna = createComuna(region);
        Aerodromo aerodromo = createAerodromo(comuna);
        when(aerodromoRepository.findAll()).thenReturn(List.of(aerodromo));

        var resultado = aerodromoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(aerodromo.getNombre_aerodromo(), resultado.get(0).getNombre_aerodromo());
    }

    @Test
    public void testBuscarPorId() {
        Region region = createRegion();
        Comuna comuna = createComuna(region);
        Aerodromo aerodromo = createAerodromo(comuna);
        when(aerodromoRepository.findById(1)).thenReturn(Optional.of(aerodromo));

        var resultado = aerodromoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(aerodromo.getNombre_aerodromo(), resultado.getNombre_aerodromo());
        verify(aerodromoRepository, times(1)).findById(1);
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(aerodromoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> aerodromoService.buscarPorId(99));
    }

    @Test
    public void testGuardarAerodromo() {
        Region region = createRegion();
        Comuna comuna = createComuna(region);
        Aerodromo aerodromo = createAerodromo(comuna);
        when(comunaRepository.findById(1)).thenReturn(Optional.of(comuna));
        when(aerodromoRepository.save(any(Aerodromo.class))).thenReturn(aerodromo);

        var resultado = aerodromoService.guardarAerodromo(aerodromo);

        assertNotNull(resultado);
        assertEquals(aerodromo.getNombre_aerodromo(), resultado.getNombre_aerodromo());
        verify(aerodromoRepository, times(1)).save(any(Aerodromo.class));
    }

    @Test
    public void testGuardarAerodromoComunaNoExiste() {
        Region region = createRegion();
        Comuna comuna = createComuna(region);
        Aerodromo aerodromo = createAerodromo(comuna);
        when(comunaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> aerodromoService.guardarAerodromo(aerodromo));
    }

    @Test
    public void testActualizarAerodromo() {
        Region region = createRegion();
        Comuna comuna = createComuna(region);
        Aerodromo existente = createAerodromo(comuna);
        String nuevoNombre = faker.regexify("[A-Za-z ]{5,15}");
        existente.setNombre_aerodromo(nuevoNombre); // el service muta existente antes del save

        Aerodromo actualizado = createAerodromo(comuna);
        actualizado.setNombre_aerodromo(nuevoNombre);

        when(aerodromoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(comunaRepository.findById(1)).thenReturn(Optional.of(comuna));
        when(aerodromoRepository.save(any(Aerodromo.class))).thenReturn(actualizado);

        var resultado = aerodromoService.actualizarAerodromo(1, actualizado);

        assertNotNull(resultado);
        assertEquals(nuevoNombre, resultado.getNombre_aerodromo());
        verify(aerodromoRepository, times(1)).save(any(Aerodromo.class));
    }

    @Test
    public void testActualizarAerodromoNoExiste() {
        when(aerodromoRepository.findById(99)).thenReturn(Optional.empty());
        Aerodromo actualizado = createAerodromo(createComuna(createRegion()));

        assertThrows(RuntimeException.class, () -> aerodromoService.actualizarAerodromo(99, actualizado));
    }

    @Test
    public void testPatchAerodromo() {
        Region region = createRegion();
        Comuna comuna = createComuna(region);
        Aerodromo existente = createAerodromo(comuna);
        String nuevoNombre = faker.regexify("[A-Za-z ]{5,15}");
        existente.setNombre_aerodromo(nuevoNombre); // patch muta existente antes del save

        Aerodromo patch = new Aerodromo();
        patch.setNombre_aerodromo(nuevoNombre);

        when(aerodromoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(aerodromoRepository.save(any(Aerodromo.class))).thenReturn(existente);

        var resultado = aerodromoService.patchAerodromo(1, patch);

        assertNotNull(resultado);
        assertEquals(nuevoNombre, resultado.getNombre_aerodromo());
        verify(aerodromoRepository, times(1)).save(any(Aerodromo.class));
    }

    @Test
    public void testPatchAerodromoNoExiste() {
        when(aerodromoRepository.findById(99)).thenReturn(Optional.empty());
        Aerodromo patch = new Aerodromo();
        patch.setNombre_aerodromo(faker.regexify("[A-Za-z ]{5,15}"));

        assertThrows(RuntimeException.class, () -> aerodromoService.patchAerodromo(99, patch));
    }

    @Test
    public void testEliminar() {
        Region region = createRegion();
        Comuna comuna = createComuna(region);
        Aerodromo aerodromo = createAerodromo(comuna);
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