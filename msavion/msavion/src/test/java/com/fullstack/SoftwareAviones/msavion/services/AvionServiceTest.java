package com.fullstack.SoftwareAviones.msavion.services;

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

import com.fullstack.SoftwareAviones.msavion.model.Avion;
import com.fullstack.SoftwareAviones.msavion.model.Fabricante;
import com.fullstack.SoftwareAviones.msavion.model.Origen;
import com.fullstack.SoftwareAviones.msavion.model.Tipo;
import com.fullstack.SoftwareAviones.msavion.model.TipoAvion;
import com.fullstack.SoftwareAviones.msavion.repository.AvionRepository;
import com.fullstack.SoftwareAviones.msavion.repository.FabricanteRepository;
import com.fullstack.SoftwareAviones.msavion.repository.OrigenRepository;
import com.fullstack.SoftwareAviones.msavion.repository.TipoRepository;

@SpringBootTest
public class AvionServiceTest {

    @Autowired
    private AvionService avionService;

    @MockitoBean
    private AvionRepository avionRepository;

    @MockitoBean
    private FabricanteRepository fabricanteRepository;

    @MockitoBean
    private OrigenRepository origenRepository;

    @MockitoBean
    private TipoRepository tipoRepository;

    private final Faker faker = new Faker();

    private Fabricante createFabricante() {
        Fabricante fabricante = new Fabricante();
        fabricante.setId_fabricante(1);
        fabricante.setNombre_fabricante(faker.regexify("[A-Za-z]{5,10}"));
        fabricante.setAviones(List.of());
        return fabricante;
    }

    private Origen createOrigen() {
        Origen origen = new Origen();
        origen.setId_origen(1);
        origen.setPais_origen(faker.regexify("[A-Za-z]{3,10}"));
        origen.setAviones(List.of());
        return origen;
    }

    private Tipo createTipo(TipoAvion tipoAvion) {
        Tipo tipo = new Tipo();
        tipo.setId_tipo(1);
        tipo.setTipo(tipoAvion);
        tipo.setAviones(List.of());
        return tipo;
    }

    private Avion createAvion(TipoAvion tipoAvion) {
        Avion avion = new Avion();
        avion.setID_avion(1);
        avion.setMatricula(faker.regexify("[A-Z]{2}-[A-Z]{3}"));
        avion.setModelo(faker.regexify("[A-Za-z]{3}-[0-9]{3}"));
        avion.setEnvergadura_metros(faker.number().randomDouble(1, 10, 80));
        avion.setCapacidad_combustible(faker.number().randomDouble(0, 5000, 100000));
        avion.setFabricante(createFabricante());
        avion.setOrigen(createOrigen());
        avion.setTipo(createTipo(tipoAvion));

        switch (tipoAvion) {
            case PASAJERO -> avion.setCapacidad_pasajero(faker.number().numberBetween(50, 500));
            case GUERRA   -> avion.setAlcance_km(faker.number().randomDouble(0, 500, 10000));
            case CARGA    -> avion.setCapacidad_carga_kg(faker.number().randomDouble(0, 1000, 100000));
            case PRIVADO  -> avion.setCantidad_asientos_vip(faker.number().numberBetween(2, 20));
        }

        return avion;
    }

    @Test
    public void testObtenerTodos() {
        Avion avion = createAvion(TipoAvion.PASAJERO);
        when(avionRepository.findAll()).thenReturn(List.of(avion));

        var resultado = avionService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(avion.getMatricula(), resultado.get(0).getMatricula());
    }

    @Test
    public void testBuscarPorId() {
        Avion avion = createAvion(TipoAvion.PASAJERO);
        when(avionRepository.findById(1)).thenReturn(Optional.of(avion));

        var resultado = avionService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(avion.getMatricula(), resultado.getMatricula());
        assertEquals("PASAJERO", resultado.getTipo());
        verify(avionRepository, times(1)).findById(1);
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(avionRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> avionService.buscarPorId(99));
    }

    @Test
    public void testGuardarAvionPasajero() {
        Avion avion = createAvion(TipoAvion.PASAJERO);
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(avion.getFabricante()));
        when(origenRepository.findById(1)).thenReturn(Optional.of(avion.getOrigen()));
        when(tipoRepository.findById(1)).thenReturn(Optional.of(createTipo(TipoAvion.PASAJERO)));
        when(avionRepository.save(any(Avion.class))).thenReturn(avion);

        var resultado = avionService.guardarAvion(avion);

        assertNotNull(resultado);
        assertEquals(avion.getMatricula(), resultado.getMatricula());
        assertEquals("PASAJERO", resultado.getTipo());
        verify(avionRepository, times(1)).save(any(Avion.class));
    }

    @Test
    public void testGuardarAvionPasajeroSinCapacidad() {
        Avion avion = createAvion(TipoAvion.PASAJERO);
        avion.setCapacidad_pasajero(null);
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(avion.getFabricante()));
        when(origenRepository.findById(1)).thenReturn(Optional.of(avion.getOrigen()));
        when(tipoRepository.findById(1)).thenReturn(Optional.of(createTipo(TipoAvion.PASAJERO)));

        assertThrows(RuntimeException.class, () -> avionService.guardarAvion(avion));
    }

    @Test
    public void testGuardarAvionGuerraSinAlcance() {
        Avion avion = createAvion(TipoAvion.GUERRA);
        avion.setAlcance_km(null);
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(avion.getFabricante()));
        when(origenRepository.findById(1)).thenReturn(Optional.of(avion.getOrigen()));
        when(tipoRepository.findById(1)).thenReturn(Optional.of(createTipo(TipoAvion.GUERRA)));

        assertThrows(RuntimeException.class, () -> avionService.guardarAvion(avion));
    }

    @Test
    public void testGuardarAvionCargaSinCapacidad() {
        Avion avion = createAvion(TipoAvion.CARGA);
        avion.setCapacidad_carga_kg(null);
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(avion.getFabricante()));
        when(origenRepository.findById(1)).thenReturn(Optional.of(avion.getOrigen()));
        when(tipoRepository.findById(1)).thenReturn(Optional.of(createTipo(TipoAvion.CARGA)));

        assertThrows(RuntimeException.class, () -> avionService.guardarAvion(avion));
    }

    @Test
    public void testGuardarAvionPrivadoSinAsientosVip() {
        Avion avion = createAvion(TipoAvion.PRIVADO);
        avion.setCantidad_asientos_vip(null);
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(avion.getFabricante()));
        when(origenRepository.findById(1)).thenReturn(Optional.of(avion.getOrigen()));
        when(tipoRepository.findById(1)).thenReturn(Optional.of(createTipo(TipoAvion.PRIVADO)));

        assertThrows(RuntimeException.class, () -> avionService.guardarAvion(avion));
    }

    @Test
    public void testEliminar() {
        Avion avion = createAvion(TipoAvion.PASAJERO);
        when(avionRepository.findById(1)).thenReturn(Optional.of(avion));
        doNothing().when(avionRepository).delete(avion);

        String resultado = avionService.eliminar(1);

        assertTrue(resultado.contains("correctamente"));
        verify(avionRepository, times(1)).delete(avion);
    }

    @Test
    public void testEliminarNoExiste() {
        when(avionRepository.findById(99)).thenReturn(Optional.empty());

        String resultado = avionService.eliminar(99);

        assertTrue(resultado.contains("no existe"));
    }

    @Test
    public void testBuscarPorMatricula() {
        Avion avion = createAvion(TipoAvion.PASAJERO);
        String matricula = avion.getMatricula();
        when(avionRepository.buscarPorMatricula(matricula)).thenReturn(List.of(avion));

        var resultado = avionService.buscarPorMatricula(matricula);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(matricula, resultado.get(0).getMatricula());
    }

    @Test
    public void testBuscarPorMatriculaNoExiste() {
        String matriculaInexistente = faker.regexify("[A-Z]{2}-[A-Z]{3}");
        when(avionRepository.buscarPorMatricula(matriculaInexistente)).thenReturn(List.of());

        var resultado = avionService.buscarPorMatricula(matriculaInexistente);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testActualizarAvion() {
        Avion existente = createAvion(TipoAvion.PASAJERO);
        Avion actualizado = createAvion(TipoAvion.PASAJERO);
        String nuevoModelo = faker.regexify("[A-Za-z]{3}-[0-9]{3}");
        actualizado.setModelo(nuevoModelo);

        when(avionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(actualizado.getFabricante()));
        when(origenRepository.findById(1)).thenReturn(Optional.of(actualizado.getOrigen()));
        when(tipoRepository.findById(1)).thenReturn(Optional.of(createTipo(TipoAvion.PASAJERO)));
        when(avionRepository.save(any(Avion.class))).thenReturn(actualizado);

        var resultado = avionService.actualizarAvion(1, actualizado);

        assertNotNull(resultado);
        assertEquals(nuevoModelo, resultado.getModelo());
        verify(avionRepository, times(1)).save(any(Avion.class));
    }

    @Test
    public void testActualizarAvionNoExiste() {
        when(avionRepository.findById(99)).thenReturn(Optional.empty());
        Avion actualizado = createAvion(TipoAvion.PASAJERO);

        assertThrows(RuntimeException.class, () -> avionService.actualizarAvion(99, actualizado));
    }

    @Test
    public void testPatchAvion() {
        Avion existente = createAvion(TipoAvion.PASAJERO);
        String nuevoModelo = faker.regexify("[A-Za-z]{3}-[0-9]{3}");
        existente.setModelo(nuevoModelo);

        Avion patch = new Avion();
        patch.setModelo(nuevoModelo);

        when(avionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(avionRepository.save(any(Avion.class))).thenReturn(existente);

        var resultado = avionService.patchAvion(1, patch);

        assertNotNull(resultado);
        assertEquals(nuevoModelo, resultado.getModelo());
        verify(avionRepository, times(1)).save(any(Avion.class));
    }

    @Test
    public void testPatchAvionNoExiste() {
        when(avionRepository.findById(99)).thenReturn(Optional.empty());
        Avion patch = new Avion();
        patch.setModelo(faker.regexify("[A-Za-z]{3}-[0-9]{3}"));

        assertThrows(RuntimeException.class, () -> avionService.patchAvion(99, patch));
    }
}