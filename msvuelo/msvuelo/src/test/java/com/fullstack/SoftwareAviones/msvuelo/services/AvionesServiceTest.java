package com.fullstack.SoftwareAviones.msvuelo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import net.datafaker.Faker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;

import com.fullstack.SoftwareAviones.msvuelo.DTO.AvionDTO;
import com.fullstack.SoftwareAviones.msvuelo.DTO.PilotoDTO;
import com.fullstack.SoftwareAviones.msvuelo.model.Aviones;
import com.fullstack.SoftwareAviones.msvuelo.repository.AvionesRepository;

import reactor.core.publisher.Mono;

@SpringBootTest
public class AvionesServiceTest {

    @Autowired
    private AvionesService avionesService;

    @MockitoBean
    private AvionesRepository avionesRepository;

    @MockitoBean
    private WebClient.Builder webClientBuilder;

    private final Faker faker = new Faker();

    // mocks internos de la cadena WebClient
    private WebClient webClient;
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp() {
        webClient             = mock(WebClient.class);
        requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersSpec    = mock(WebClient.RequestHeadersSpec.class);
        responseSpec          = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        doReturn(requestHeadersUriSpec).when(webClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString());
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    }

    // aca parte la zona de los create

    private Aviones createAviones() {
        Aviones aviones = new Aviones();
        aviones.setID_aviones(1);
        aviones.setIdPiloto(faker.number().numberBetween(1, 100));
        aviones.setIdAvion(faker.number().numberBetween(1, 100));
        return aviones;
    }

    private PilotoDTO createPiloto(int idPiloto) {
        PilotoDTO piloto = new PilotoDTO();
        piloto.setID_piloto(idPiloto);
        piloto.setNombre(faker.regexify("[A-Za-z]{3,10}"));
        piloto.setApellido(faker.regexify("[A-Za-z]{3,10}"));
        return piloto;
    }

    private AvionDTO createAvion(int idAvion) {
        AvionDTO avion = new AvionDTO();
        avion.setID_avion(idAvion);
        avion.setModelo(faker.regexify("[A-Za-z]{5,10}-[0-9]{3}"));
        avion.setMatricula(faker.regexify("[A-Z]{2}-[A-Z]{3}"));
        return avion;
    }

    // helper para configurar la cadena WebClient con las respuestas de piloto y avion
    @SuppressWarnings("unchecked")
    private void mockWebClient(PilotoDTO piloto, AvionDTO avion) {
        when(responseSpec.bodyToMono(PilotoDTO.class))
                .thenReturn(piloto != null ? Mono.just(piloto) : Mono.error(new RuntimeException("Servicio mspiloto caido")));
        when(responseSpec.bodyToMono(AvionDTO.class))
                .thenReturn(avion != null ? Mono.just(avion) : Mono.error(new RuntimeException("Servicio msavion caido")));
    }

    // aca parten los test

    @Test
    public void testObtenerTodos() {
        Aviones aviones = createAviones();
        PilotoDTO piloto = createPiloto(aviones.getIdPiloto());
        AvionDTO avion   = createAvion(aviones.getIdAvion());

        when(avionesRepository.findAll()).thenReturn(List.of(aviones));
        mockWebClient(piloto, avion);

        var resultado = avionesService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(piloto.getNombre(), resultado.get(0).getNombrePiloto());
        assertEquals(avion.getModelo(), resultado.get(0).getModeloAvion());
    }

    @Test
    public void testBuscarPorId() {
        Aviones aviones = createAviones();
        PilotoDTO piloto = createPiloto(aviones.getIdPiloto());
        AvionDTO avion   = createAvion(aviones.getIdAvion());

        when(avionesRepository.findById(1)).thenReturn(Optional.of(aviones));
        mockWebClient(piloto, avion);

        var resultado = avionesService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(aviones.getIdPiloto(), resultado.getIdPiloto());
        assertEquals(aviones.getIdAvion(), resultado.getIdAvion());
        assertEquals(piloto.getNombre(), resultado.getNombrePiloto());
        assertEquals(avion.getModelo(), resultado.getModeloAvion());
        verify(avionesRepository, times(1)).findById(1);
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(avionesRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> avionesService.buscarPorId(99));
    }

    @Test
    public void testBuscarPorIdPilotoNoDisponible() {
        Aviones aviones = createAviones();
        AvionDTO avion  = createAvion(aviones.getIdAvion());

        when(avionesRepository.findById(1)).thenReturn(Optional.of(aviones));
        mockWebClient(null, avion); // piloto null → lanza excepcion → fallback

        var resultado = avionesService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Piloto no disponible", resultado.getNombrePiloto());
        assertEquals(avion.getModelo(), resultado.getModeloAvion());
    }

    @Test
    public void testBuscarPorIdAvionNoDisponible() {
        Aviones aviones  = createAviones();
        PilotoDTO piloto = createPiloto(aviones.getIdPiloto());

        when(avionesRepository.findById(1)).thenReturn(Optional.of(aviones));
        mockWebClient(piloto, null); // avion null → lanza excepcion → fallback

        var resultado = avionesService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(piloto.getNombre(), resultado.getNombrePiloto());
        assertEquals("Avion no disponible", resultado.getModeloAvion());
    }

    @Test
    public void testGuardarAviones() {
        Aviones aviones  = createAviones();
        PilotoDTO piloto = createPiloto(aviones.getIdPiloto());
        AvionDTO avion   = createAvion(aviones.getIdAvion());

        when(avionesRepository.save(any(Aviones.class))).thenReturn(aviones);
        mockWebClient(piloto, avion);

        var resultado = avionesService.guardarAviones(aviones);

        assertNotNull(resultado);
        assertEquals(aviones.getIdPiloto(), resultado.getIdPiloto());
        assertEquals(aviones.getIdAvion(), resultado.getIdAvion());
        verify(avionesRepository, times(1)).save(aviones);
    }

    @Test
    public void testActualizarAviones() {
        Aviones existente  = createAviones();
        Aviones actualizado = createAviones();
        int nuevoIdPiloto  = faker.number().numberBetween(101, 200);
        int nuevoIdAvion   = faker.number().numberBetween(101, 200);
        actualizado.setIdPiloto(nuevoIdPiloto);
        actualizado.setIdAvion(nuevoIdAvion);

        PilotoDTO piloto = createPiloto(nuevoIdPiloto);
        AvionDTO avion   = createAvion(nuevoIdAvion);

        when(avionesRepository.findById(1)).thenReturn(Optional.of(existente));
        when(avionesRepository.save(any(Aviones.class))).thenReturn(actualizado);
        mockWebClient(piloto, avion);

        var resultado = avionesService.actualizarAviones(1, actualizado);

        assertNotNull(resultado);
        assertEquals(nuevoIdPiloto, resultado.getIdPiloto());
        assertEquals(nuevoIdAvion, resultado.getIdAvion());
        verify(avionesRepository, times(1)).save(any(Aviones.class));
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