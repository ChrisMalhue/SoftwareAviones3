package com.fullstack.SoftwareAviones.msvuelo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import com.fullstack.SoftwareAviones.msvuelo.DTO.AvionDTO;
import com.fullstack.SoftwareAviones.msvuelo.DTO.PilotoDTO;
import com.fullstack.SoftwareAviones.msvuelo.model.Aviones;
import com.fullstack.SoftwareAviones.msvuelo.repository.AvionesRepository;

@SpringBootTest
public class AvionesServiceTest {

    @Autowired
    private AvionesService avionesService;

    @MockitoBean
    private AvionesRepository avionesRepository;

    @MockitoBean
    private RestTemplate restTemplate;

    private Aviones createAviones() {
        Aviones aviones = new Aviones();
        aviones.setID_aviones(1);
        aviones.setIdPiloto(10);
        aviones.setIdAvion(20);
        return aviones;
    }

    private PilotoDTO createPiloto() {
        PilotoDTO piloto = new PilotoDTO();
        piloto.setID_piloto(10);
        piloto.setNombre("Juan");
        piloto.setApellido("Perez");
        return piloto;
    }

    private AvionDTO createAvion() {
        AvionDTO avion = new AvionDTO();
        avion.setID_avion(20);
        avion.setModelo("Boeing 737");
        avion.setMatricula("CC-ABC");
        return avion;
    }

    @Test
    public void testObtenerTodos() {
        when(avionesRepository.findAll()).thenReturn(List.of(createAviones()));
        when(restTemplate.getForObject(anyString(), eq(PilotoDTO.class))).thenReturn(createPiloto());
        when(restTemplate.getForObject(anyString(), eq(AvionDTO.class))).thenReturn(createAvion());

        var resultado = avionesService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombrePiloto());
        assertEquals("Boeing 737", resultado.get(0).getModeloAvion());
    }

    @Test
    public void testBuscarPorId() {
        when(avionesRepository.findById(1)).thenReturn(Optional.of(createAviones()));
        when(restTemplate.getForObject(anyString(), eq(PilotoDTO.class))).thenReturn(createPiloto());
        when(restTemplate.getForObject(anyString(), eq(AvionDTO.class))).thenReturn(createAvion());

        var resultado = avionesService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(10, resultado.getIdPiloto());
        assertEquals(20, resultado.getIdAvion());
        assertEquals("Juan", resultado.getNombrePiloto());
        assertEquals("Boeing 737", resultado.getModeloAvion());
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(avionesRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> avionesService.buscarPorId(99));
    }

    @Test
    public void testBuscarPorIdPilotoNoDisponible() {
        when(avionesRepository.findById(1)).thenReturn(Optional.of(createAviones()));
        when(restTemplate.getForObject(anyString(), eq(PilotoDTO.class)))
                .thenThrow(new RuntimeException("Servicio mspiloto caido"));
        when(restTemplate.getForObject(anyString(), eq(AvionDTO.class))).thenReturn(createAvion());

        var resultado = avionesService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Piloto no disponible", resultado.getNombrePiloto());
        assertEquals("Boeing 737", resultado.getModeloAvion());
    }

    @Test
    public void testBuscarPorIdAvionNoDisponible() {
        when(avionesRepository.findById(1)).thenReturn(Optional.of(createAviones()));
        when(restTemplate.getForObject(anyString(), eq(PilotoDTO.class))).thenReturn(createPiloto());
        when(restTemplate.getForObject(anyString(), eq(AvionDTO.class)))
                .thenThrow(new RuntimeException("Servicio msavion caido"));

        var resultado = avionesService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombrePiloto());
        assertEquals("Avion no disponible", resultado.getModeloAvion());
    }

    @Test
    public void testGuardarAviones() {
        Aviones aviones = createAviones();
        when(avionesRepository.save(any(Aviones.class))).thenReturn(aviones);
        when(restTemplate.getForObject(anyString(), eq(PilotoDTO.class))).thenReturn(createPiloto());
        when(restTemplate.getForObject(anyString(), eq(AvionDTO.class))).thenReturn(createAvion());

        var resultado = avionesService.guardarAviones(aviones);

        assertNotNull(resultado);
        assertEquals(10, resultado.getIdPiloto());
        assertEquals(20, resultado.getIdAvion());
        verify(avionesRepository, times(1)).save(aviones);
    }

    @Test
    public void testActualizarAviones() {
        Aviones existente = createAviones();
        Aviones actualizado = createAviones();
        actualizado.setIdPiloto(11);
        actualizado.setIdAvion(21);

        when(avionesRepository.findById(1)).thenReturn(Optional.of(existente));
        when(avionesRepository.save(any(Aviones.class))).thenReturn(actualizado);
        when(restTemplate.getForObject(anyString(), eq(PilotoDTO.class))).thenReturn(createPiloto());
        when(restTemplate.getForObject(anyString(), eq(AvionDTO.class))).thenReturn(createAvion());

        var resultado = avionesService.actualizarAviones(1, actualizado);

        assertNotNull(resultado);
        assertEquals(11, resultado.getIdPiloto());
        assertEquals(21, resultado.getIdAvion());
    }

    @Test
    public void testActualizarAvionesNoExiste() {
        when(avionesRepository.findById(99)).thenReturn(Optional.empty());
        Aviones actualizado = createAviones();
        assertThrows(RuntimeException.class, () -> avionesService.actualizarAviones(99, actualizado));
    }

    @Test
    public void testEliminar() {
        Aviones aviones = createAviones();
        when(avionesRepository.findById(1)).thenReturn(Optional.of(aviones));
        doNothing().when(avionesRepository).delete(aviones);

        String resultado = avionesService.eliminar(1);

        assertTrue(resultado.contains("correctamente"));
        verify(avionesRepository, times(1)).delete(aviones);
    }

    @Test
    public void testEliminarNoExiste() {
        when(avionesRepository.findById(99)).thenReturn(Optional.empty());
        String resultado = avionesService.eliminar(99);
        assertTrue(resultado.contains("no existe"));
    }
}