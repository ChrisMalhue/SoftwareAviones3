package com.fullstack.SoftwareAviones.msvuelo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import com.fullstack.SoftwareAviones.msvuelo.DTO.AerodromoDTO;
import com.fullstack.SoftwareAviones.msvuelo.DTO.AvionDTO;
import com.fullstack.SoftwareAviones.msvuelo.DTO.PilotoDTO;
import com.fullstack.SoftwareAviones.msvuelo.model.Vuelo;
import com.fullstack.SoftwareAviones.msvuelo.repository.VueloRepository;

@SpringBootTest
public class VueloServiceTest {

    @Autowired
    private VueloService vueloService;

    @MockitoBean
    private VueloRepository vueloRepository;

    @MockitoBean
    private RestTemplate restTemplate;

    private Vuelo createVuelo() throws Exception {
        Vuelo vuelo = new Vuelo();
        vuelo.setID_vuelo(1);
        vuelo.setNumero_vuelo("AV123");
        vuelo.setHora_inicio_vuelo(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2026-07-15T08:00:00"));
        vuelo.setTipo_vuelo("Internacional");
        vuelo.setDestino("Buenos Aires");
        vuelo.setIdPiloto(10);
        vuelo.setIdAvion(20);
        vuelo.setIdAerodromo(30);
        return vuelo;
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

    private AerodromoDTO createAerodromoDTO() {
        AerodromoDTO aerodromo = new AerodromoDTO();
        aerodromo.setID_aerodromo(30);
        aerodromo.setNombre_aerodromo("Aerodromo El Bosque");
        return aerodromo;
    }

    @Test
    public void testObtenerTodos() throws Exception {
        when(vueloRepository.findAll()).thenReturn(List.of(createVuelo()));
        when(restTemplate.getForObject(anyString(), eq(PilotoDTO.class))).thenReturn(createPiloto());
        when(restTemplate.getForObject(anyString(), eq(AvionDTO.class))).thenReturn(createAvion());
        when(restTemplate.getForObject(anyString(), eq(AerodromoDTO.class))).thenReturn(createAerodromoDTO());

        var resultado = vueloService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("AV123", resultado.get(0).getNumero_vuelo());
        assertEquals("Juan", resultado.get(0).getNombrePiloto());
        assertEquals("Boeing 737", resultado.get(0).getModeloAvion());
        assertEquals("Aerodromo El Bosque", resultado.get(0).getNombreAerodromo());
    }

    @Test
    public void testBuscarPorId() throws Exception {
        when(vueloRepository.findById(1)).thenReturn(Optional.of(createVuelo()));
        when(restTemplate.getForObject(anyString(), eq(PilotoDTO.class))).thenReturn(createPiloto());
        when(restTemplate.getForObject(anyString(), eq(AvionDTO.class))).thenReturn(createAvion());
        when(restTemplate.getForObject(anyString(), eq(AerodromoDTO.class))).thenReturn(createAerodromoDTO());

        var resultado = vueloService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("AV123", resultado.getNumero_vuelo());
        assertEquals("Buenos Aires", resultado.getDestino());
        assertEquals("Juan", resultado.getNombrePiloto());
        assertEquals("Boeing 737", resultado.getModeloAvion());
        assertEquals("Aerodromo El Bosque", resultado.getNombreAerodromo());
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(vueloRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> vueloService.buscarPorId(99));
    }

    @Test
    public void testBuscarPorIdPilotoNoDisponible() throws Exception {
        when(vueloRepository.findById(1)).thenReturn(Optional.of(createVuelo()));
        when(restTemplate.getForObject(anyString(), eq(PilotoDTO.class)))
                .thenThrow(new RuntimeException("Servicio mspiloto caido"));
        when(restTemplate.getForObject(anyString(), eq(AvionDTO.class))).thenReturn(createAvion());
        when(restTemplate.getForObject(anyString(), eq(AerodromoDTO.class))).thenReturn(createAerodromoDTO());

        var resultado = vueloService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Piloto no disponible", resultado.getNombrePiloto());
        assertEquals("Boeing 737", resultado.getModeloAvion());
        assertEquals("Aerodromo El Bosque", resultado.getNombreAerodromo());
    }

    @Test
    public void testBuscarPorIdAvionNoDisponible() throws Exception {
        when(vueloRepository.findById(1)).thenReturn(Optional.of(createVuelo()));
        when(restTemplate.getForObject(anyString(), eq(PilotoDTO.class))).thenReturn(createPiloto());
        when(restTemplate.getForObject(anyString(), eq(AvionDTO.class)))
                .thenThrow(new RuntimeException("Servicio msavion caido"));
        when(restTemplate.getForObject(anyString(), eq(AerodromoDTO.class))).thenReturn(createAerodromoDTO());

        var resultado = vueloService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombrePiloto());
        assertEquals("Avion no disponible", resultado.getModeloAvion());
        assertEquals("Aerodromo El Bosque", resultado.getNombreAerodromo());
    }

    @Test
    public void testBuscarPorIdAerodromoNoDisponible() throws Exception {
        when(vueloRepository.findById(1)).thenReturn(Optional.of(createVuelo()));
        when(restTemplate.getForObject(anyString(), eq(PilotoDTO.class))).thenReturn(createPiloto());
        when(restTemplate.getForObject(anyString(), eq(AvionDTO.class))).thenReturn(createAvion());
        when(restTemplate.getForObject(anyString(), eq(AerodromoDTO.class)))
                .thenThrow(new RuntimeException("Servicio ms-ubicacion caido"));

        var resultado = vueloService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombrePiloto());
        assertEquals("Boeing 737", resultado.getModeloAvion());
        assertEquals("Aerodromo no disponible", resultado.getNombreAerodromo());
    }

    @Test
    public void testAgregarVuelo() throws Exception {
        Vuelo vuelo = createVuelo();
        when(vueloRepository.save(any(Vuelo.class))).thenReturn(vuelo);
        when(restTemplate.getForObject(anyString(), eq(PilotoDTO.class))).thenReturn(createPiloto());
        when(restTemplate.getForObject(anyString(), eq(AvionDTO.class))).thenReturn(createAvion());
        when(restTemplate.getForObject(anyString(), eq(AerodromoDTO.class))).thenReturn(createAerodromoDTO());

        var resultado = vueloService.agregarVuelo(vuelo);

        assertNotNull(resultado);
        assertEquals("AV123", resultado.getNumero_vuelo());
        verify(vueloRepository, times(1)).save(vuelo);
    }

    @Test
    public void testActualizarVuelo() throws Exception {
        Vuelo existente = createVuelo();
        Vuelo actualizado = createVuelo();
        actualizado.setNumero_vuelo("LA456");
        actualizado.setDestino("Santiago de Chile");

        when(vueloRepository.findById(1)).thenReturn(Optional.of(existente));
        when(vueloRepository.save(any(Vuelo.class))).thenReturn(actualizado);
        when(restTemplate.getForObject(anyString(), eq(PilotoDTO.class))).thenReturn(createPiloto());
        when(restTemplate.getForObject(anyString(), eq(AvionDTO.class))).thenReturn(createAvion());
        when(restTemplate.getForObject(anyString(), eq(AerodromoDTO.class))).thenReturn(createAerodromoDTO());

        var resultado = vueloService.actualizarVuelo(1, actualizado);

        assertNotNull(resultado);
        assertEquals("LA456", resultado.getNumero_vuelo());
        assertEquals("Santiago de Chile", resultado.getDestino());
    }

    @Test
    public void testActualizarVueloNoExiste() throws Exception {
        when(vueloRepository.findById(99)).thenReturn(Optional.empty());
        Vuelo actualizado = createVuelo();
        assertThrows(RuntimeException.class, () -> vueloService.actualizarVuelo(99, actualizado));
    }

    @Test
    public void testPatchVuelo() throws Exception {
        Vuelo existente = createVuelo();
        Vuelo patch = new Vuelo();
        patch.setDestino("Lima");

        when(vueloRepository.findById(1)).thenReturn(Optional.of(existente));
        when(vueloRepository.save(any(Vuelo.class))).thenReturn(existente);
        when(restTemplate.getForObject(anyString(), eq(PilotoDTO.class))).thenReturn(createPiloto());
        when(restTemplate.getForObject(anyString(), eq(AvionDTO.class))).thenReturn(createAvion());
        when(restTemplate.getForObject(anyString(), eq(AerodromoDTO.class))).thenReturn(createAerodromoDTO());

        var resultado = vueloService.patchVuelo(1, patch);

        assertNotNull(resultado);
        assertEquals("Lima", resultado.getDestino());
    }

    @Test
    public void testPatchVueloNoExiste() {
        when(vueloRepository.findById(99)).thenReturn(Optional.empty());
        Vuelo patch = new Vuelo();
        patch.setDestino("Lima");
        assertThrows(RuntimeException.class, () -> vueloService.patchVuelo(99, patch));
    }

    @Test
    public void testEliminar() throws Exception {
        Vuelo vuelo = createVuelo();
        when(vueloRepository.findById(1)).thenReturn(Optional.of(vuelo));
        doNothing().when(vueloRepository).delete(vuelo);

        String resultado = vueloService.eliminar(1);

        assertTrue(resultado.contains("exitosamente"));
        verify(vueloRepository, times(1)).delete(vuelo);
    }

    @Test
    public void testEliminarNoExiste() {
        when(vueloRepository.findById(99)).thenReturn(Optional.empty());
        String resultado = vueloService.eliminar(99);
        assertTrue(resultado.contains("no existe"));
    }
}