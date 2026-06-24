package com.fullstack.SoftwareAviones.msvuelo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import net.datafaker.Faker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;

import com.fullstack.SoftwareAviones.msvuelo.DTO.AerodromoDTO;
import com.fullstack.SoftwareAviones.msvuelo.DTO.AvionDTO;
import com.fullstack.SoftwareAviones.msvuelo.DTO.PilotoDTO;
import com.fullstack.SoftwareAviones.msvuelo.model.Vuelo;
import com.fullstack.SoftwareAviones.msvuelo.repository.VueloRepository;

import reactor.core.publisher.Mono;

@SpringBootTest
public class VueloServiceTest {

    @Autowired
    private VueloService vueloService;

    @MockitoBean
    private VueloRepository vueloRepository;

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

    private Vuelo createVuelo() {
        Vuelo vuelo = new Vuelo();
        vuelo.setID_vuelo(1);
        vuelo.setNumero_vuelo(faker.regexify("[A-Z]{2}[0-9]{3}"));
        vuelo.setHora_inicio_vuelo(new Date());
        vuelo.setTipo_vuelo(faker.regexify("(Nacional|Internacional)"));
        vuelo.setDestino(faker.regexify("[A-Za-z]{5,12}"));
        vuelo.setIdPiloto(faker.number().numberBetween(1, 100));
        vuelo.setIdAvion(faker.number().numberBetween(1, 100));
        vuelo.setIdAerodromo(faker.number().numberBetween(1, 100));
        return vuelo;
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

    private AerodromoDTO createAerodromo(int idAerodromo) {
        AerodromoDTO aerodromo = new AerodromoDTO();
        aerodromo.setID_aerodromo(idAerodromo);
        aerodromo.setNombre_aerodromo(faker.regexify("[A-Za-z]{5,15}"));
        return aerodromo;
    }

    // helper: configura bodyToMono para los 3 servicios externos
    // null en cualquiera activa el fallback del catch correspondiente
    @SuppressWarnings("unchecked")
    private void mockWebClient(PilotoDTO piloto, AvionDTO avion, AerodromoDTO aerodromo) {
        when(responseSpec.bodyToMono(PilotoDTO.class))
                .thenReturn(piloto != null ? Mono.just(piloto) : Mono.error(new RuntimeException("Servicio mspiloto caido")));
        when(responseSpec.bodyToMono(AvionDTO.class))
                .thenReturn(avion != null ? Mono.just(avion) : Mono.error(new RuntimeException("Servicio msavion caido")));
        when(responseSpec.bodyToMono(AerodromoDTO.class))
                .thenReturn(aerodromo != null ? Mono.just(aerodromo) : Mono.error(new RuntimeException("Servicio msubicacion caido")));
    }

    // aca parten los test

    @Test
    public void testObtenerTodos() {
        Vuelo vuelo          = createVuelo();
        PilotoDTO piloto     = createPiloto(vuelo.getIdPiloto());
        AvionDTO avion       = createAvion(vuelo.getIdAvion());
        AerodromoDTO aerodromo = createAerodromo(vuelo.getIdAerodromo());

        when(vueloRepository.findAll()).thenReturn(List.of(vuelo));
        mockWebClient(piloto, avion, aerodromo);

        var resultado = vueloService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(vuelo.getNumero_vuelo(), resultado.get(0).getNumero_vuelo());
        assertEquals(piloto.getNombre(), resultado.get(0).getNombrePiloto());
        assertEquals(avion.getModelo(), resultado.get(0).getModeloAvion());
        assertEquals(aerodromo.getNombre_aerodromo(), resultado.get(0).getNombreAerodromo());
    }

    @Test
    public void testBuscarPorId() {
        Vuelo vuelo            = createVuelo();
        PilotoDTO piloto       = createPiloto(vuelo.getIdPiloto());
        AvionDTO avion         = createAvion(vuelo.getIdAvion());
        AerodromoDTO aerodromo = createAerodromo(vuelo.getIdAerodromo());

        when(vueloRepository.findById(1)).thenReturn(Optional.of(vuelo));
        mockWebClient(piloto, avion, aerodromo);

        var resultado = vueloService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(vuelo.getNumero_vuelo(), resultado.getNumero_vuelo());
        assertEquals(vuelo.getDestino(), resultado.getDestino());
        assertEquals(piloto.getNombre(), resultado.getNombrePiloto());
        assertEquals(avion.getModelo(), resultado.getModeloAvion());
        assertEquals(aerodromo.getNombre_aerodromo(), resultado.getNombreAerodromo());
        verify(vueloRepository, times(1)).findById(1);
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(vueloRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> vueloService.buscarPorId(99));
    }

    @Test
    public void testBuscarPorIdPilotoNoDisponible() {
        Vuelo vuelo            = createVuelo();
        AvionDTO avion         = createAvion(vuelo.getIdAvion());
        AerodromoDTO aerodromo = createAerodromo(vuelo.getIdAerodromo());

        when(vueloRepository.findById(1)).thenReturn(Optional.of(vuelo));
        mockWebClient(null, avion, aerodromo);

        var resultado = vueloService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Piloto no disponible", resultado.getNombrePiloto());
        assertEquals(avion.getModelo(), resultado.getModeloAvion());
        assertEquals(aerodromo.getNombre_aerodromo(), resultado.getNombreAerodromo());
    }

    @Test
    public void testBuscarPorIdAvionNoDisponible() {
        Vuelo vuelo            = createVuelo();
        PilotoDTO piloto       = createPiloto(vuelo.getIdPiloto());
        AerodromoDTO aerodromo = createAerodromo(vuelo.getIdAerodromo());

        when(vueloRepository.findById(1)).thenReturn(Optional.of(vuelo));
        mockWebClient(piloto, null, aerodromo);

        var resultado = vueloService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(piloto.getNombre(), resultado.getNombrePiloto());
        assertEquals("Avion no disponible", resultado.getModeloAvion());
        assertEquals(aerodromo.getNombre_aerodromo(), resultado.getNombreAerodromo());
    }

    @Test
    public void testBuscarPorIdAerodromoNoDisponible() {
        Vuelo vuelo      = createVuelo();
        PilotoDTO piloto = createPiloto(vuelo.getIdPiloto());
        AvionDTO avion   = createAvion(vuelo.getIdAvion());

        when(vueloRepository.findById(1)).thenReturn(Optional.of(vuelo));
        mockWebClient(piloto, avion, null);

        var resultado = vueloService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(piloto.getNombre(), resultado.getNombrePiloto());
        assertEquals(avion.getModelo(), resultado.getModeloAvion());
        assertEquals("Aerodromo no disponible", resultado.getNombreAerodromo());
    }

    @Test
    public void testAgregarVuelo() {
        Vuelo vuelo            = createVuelo();
        PilotoDTO piloto       = createPiloto(vuelo.getIdPiloto());
        AvionDTO avion         = createAvion(vuelo.getIdAvion());
        AerodromoDTO aerodromo = createAerodromo(vuelo.getIdAerodromo());

        when(vueloRepository.save(any(Vuelo.class))).thenReturn(vuelo);
        mockWebClient(piloto, avion, aerodromo);

        var resultado = vueloService.agregarVuelo(vuelo);

        assertNotNull(resultado);
        assertEquals(vuelo.getNumero_vuelo(), resultado.getNumero_vuelo());
        verify(vueloRepository, times(1)).save(vuelo);
    }

    @Test
    public void testActualizarVuelo() {
        Vuelo existente        = createVuelo();
        Vuelo actualizado      = createVuelo();
        String nuevoNumero     = faker.regexify("[A-Z]{2}[0-9]{3}");
        String nuevoDestino    = faker.regexify("[A-Za-z]{5,12}");
        actualizado.setNumero_vuelo(nuevoNumero);
        actualizado.setDestino(nuevoDestino);

        PilotoDTO piloto       = createPiloto(actualizado.getIdPiloto());
        AvionDTO avion         = createAvion(actualizado.getIdAvion());
        AerodromoDTO aerodromo = createAerodromo(actualizado.getIdAerodromo());

        when(vueloRepository.findById(1)).thenReturn(Optional.of(existente));
        when(vueloRepository.save(any(Vuelo.class))).thenReturn(actualizado);
        mockWebClient(piloto, avion, aerodromo);

        var resultado = vueloService.actualizarVuelo(1, actualizado);

        assertNotNull(resultado);
        assertEquals(nuevoNumero, resultado.getNumero_vuelo());
        assertEquals(nuevoDestino, resultado.getDestino());
        verify(vueloRepository, times(1)).save(any(Vuelo.class));
    }

    @Test
    public void testActualizarVueloNoExiste() {
        when(vueloRepository.findById(99)).thenReturn(Optional.empty());
        Vuelo actualizado = createVuelo();
        assertThrows(RuntimeException.class, () -> vueloService.actualizarVuelo(99, actualizado));
    }

    @Test
    public void testPatchVuelo() {
        Vuelo existente        = createVuelo();
        String nuevoDestino    = faker.regexify("[A-Za-z]{5,12}");
        existente.setDestino(nuevoDestino);

        Vuelo patch            = new Vuelo();
        patch.setDestino(nuevoDestino);

        PilotoDTO piloto       = createPiloto(existente.getIdPiloto());
        AvionDTO avion         = createAvion(existente.getIdAvion());
        AerodromoDTO aerodromo = createAerodromo(existente.getIdAerodromo());

        when(vueloRepository.findById(1)).thenReturn(Optional.of(existente));
        when(vueloRepository.save(any(Vuelo.class))).thenReturn(existente);
        mockWebClient(piloto, avion, aerodromo);

        var resultado = vueloService.patchVuelo(1, patch);

        assertNotNull(resultado);
        assertEquals(nuevoDestino, resultado.getDestino());
        verify(vueloRepository, times(1)).save(any(Vuelo.class));
    }

    @Test
    public void testPatchVueloNoExiste() {
        when(vueloRepository.findById(99)).thenReturn(Optional.empty());
        Vuelo patch = new Vuelo();
        patch.setDestino(faker.regexify("[A-Za-z]{5,12}"));
        assertThrows(RuntimeException.class, () -> vueloService.patchVuelo(99, patch));
    }

    @Test
    public void testEliminar() {
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