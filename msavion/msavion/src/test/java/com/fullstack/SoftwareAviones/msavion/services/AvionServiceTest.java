package com.fullstack.SoftwareAviones.msavion.services;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

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
        
    private Fabricante createFabricante() {
        Fabricante fabricante = new Fabricante();
        fabricante.setId_fabricante(1);
        fabricante.setNombre_fabricante("Boeing");
        fabricante.setAviones(List.of());
        return fabricante;
    }

    private Origen createOrigen() {
        Origen origen = new Origen();
        origen.setId_origen(1);
        origen.setPais_origen("USA");
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
        avion.setMatricula("CC-ABC");
        avion.setMarca("Boeing");
        avion.setModelo("737");
        avion.setEnvergadura_metros(35.0);
        avion.setCapacidad_combustible(20000.0);
        avion.setFabricante(createFabricante());
        avion.setOrigen(createOrigen());
        avion.setTipo(createTipo(tipoAvion));

        switch (tipoAvion) {
            case PASAJERO -> avion.setCapacidad_pasajero(180);
            case GUERRA -> avion.setAlcance_km(3000.0);
            case CARGA -> avion.setCapacidad_carga_kg(50000.0);
            case PRIVADO -> avion.setCantidad_asientos_vip(8);
        }

        return avion;
    }
    
    @Test
    public void testObtenerTodos() {
        when(avionRepository.findAll()).thenReturn(List.of(createAvion(TipoAvion.PASAJERO)));
        var resultado = avionService.obtenerTodos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("CC-ABC", resultado.get(0).getMatricula());
    }

    @Test
    public void testBuscarPorId() {
        Avion avion = createAvion(TipoAvion.PASAJERO);
        when(avionRepository.findById(1)).thenReturn(Optional.of(avion));
        var resultado = avionService.buscarPorId(1);
        assertNotNull(resultado);
        assertEquals("CC-ABC", resultado.getMatricula());
        assertEquals("PASAJERO", resultado.getTipo());
    }
    
    @Test
    public void testBuscarPorIdNoExiste() {
        when(avionRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> avionService.buscarPorId(99));
    }

    @Test
    public void testGuardarAvionPasajero() {
        Avion avion = createAvion(TipoAvion.PASAJERO);
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(createFabricante()));
        when(origenRepository.findById(1)).thenReturn(Optional.of(createOrigen()));
        when(tipoRepository.findById(1)).thenReturn(Optional.of(createTipo(TipoAvion.PASAJERO)));
        when(avionRepository.save(any(Avion.class))).thenReturn(avion);

        var resultado = avionService.guardarAvion(avion);
        assertNotNull(resultado);
        assertEquals("CC-ABC", resultado.getMatricula());
        assertEquals("PASAJERO", resultado.getTipo());
    }

    @Test
    public void testGuardarAvionPasajeroSinCapacidad() {
        Avion avion = createAvion(TipoAvion.PASAJERO);
        avion.setCapacidad_pasajero(null);
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(createFabricante()));
        when(origenRepository.findById(1)).thenReturn(Optional.of(createOrigen()));
        when(tipoRepository.findById(1)).thenReturn(Optional.of(createTipo(TipoAvion.PASAJERO)));

        assertThrows(RuntimeException.class, () -> avionService.guardarAvion(avion));
    }

    @Test
    public void testGuardarAvionGuerraSinAlcance() {
        Avion avion = createAvion(TipoAvion.GUERRA);
        avion.setAlcance_km(null);
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(createFabricante()));
        when(origenRepository.findById(1)).thenReturn(Optional.of(createOrigen()));
        when(tipoRepository.findById(1)).thenReturn(Optional.of(createTipo(TipoAvion.GUERRA)));

        assertThrows(RuntimeException.class, () -> avionService.guardarAvion(avion));
    }

    @Test
    public void testGuardarAvionCargaSinCapacidad() {
        Avion avion = createAvion(TipoAvion.CARGA);
        avion.setCapacidad_carga_kg(null);
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(createFabricante()));
        when(origenRepository.findById(1)).thenReturn(Optional.of(createOrigen()));
        when(tipoRepository.findById(1)).thenReturn(Optional.of(createTipo(TipoAvion.CARGA)));

        assertThrows(RuntimeException.class, () -> avionService.guardarAvion(avion));
    }

    @Test
    public void testGuardarAvionPrivadoSinAsientosVip() {
        Avion avion = createAvion(TipoAvion.PRIVADO);
        avion.setCantidad_asientos_vip(null);
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(createFabricante()));
        when(origenRepository.findById(1)).thenReturn(Optional.of(createOrigen()));
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
        when(avionRepository.buscarPorMatricula("CC-ABC")).thenReturn(List.of(createAvion(TipoAvion.PASAJERO)));
        var resultado = avionService.buscarPorMatricula("CC-ABC");
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("CC-ABC", resultado.get(0).getMatricula());
    }
    
    @Test
    public void testBuscarPorMatriculaNoExiste() {
        when(avionRepository.buscarPorMatricula("XX-999")).thenReturn(List.of());
        var resultado = avionService.buscarPorMatricula("XX-999");
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testActualizarAvion() {
        Avion existente = createAvion(TipoAvion.PASAJERO);
        Avion actualizado = createAvion(TipoAvion.PASAJERO);
        actualizado.setMarca("Airbus");

        when(avionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(fabricanteRepository.findById(1)).thenReturn(Optional.of(createFabricante()));
        when(origenRepository.findById(1)).thenReturn(Optional.of(createOrigen()));
        when(tipoRepository.findById(1)).thenReturn(Optional.of(createTipo(TipoAvion.PASAJERO)));
        when(avionRepository.save(any(Avion.class))).thenReturn(actualizado);

        var resultado = avionService.actualizarAvion(1, actualizado);
        assertNotNull(resultado);
        assertEquals("Airbus", resultado.getMarca());
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
        Avion patch = new Avion();
        patch.setMarca("Airbus");

        when(avionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(avionRepository.save(any(Avion.class))).thenReturn(existente);

        var resultado = avionService.patchAvion(1, patch);
        assertNotNull(resultado);
        assertEquals("Airbus", resultado.getMarca());
    }

    @Test
    public void testPatchAvionNoExiste() {
        when(avionRepository.findById(99)).thenReturn(Optional.empty());
        Avion patch = new Avion();
        patch.setMarca("Airbus");
        assertThrows(RuntimeException.class, () -> avionService.patchAvion(99, patch));
    }

    
}
