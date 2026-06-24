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

import com.fullstack.SoftwareAviones.msavion.model.Tipo;
import com.fullstack.SoftwareAviones.msavion.model.TipoAvion;
import com.fullstack.SoftwareAviones.msavion.repository.TipoRepository;

@SpringBootTest
public class TipoServiceTest {

    @Autowired
    private TipoService tipoService;

    @MockitoBean
    private TipoRepository tipoRepository;

    // no puse datafaker aca ya que este model esta conectado a un enum con 4 valores fijos,
    // y no se puede crear uno que no sea esos 4

    private Tipo createTipo() {
        Tipo tipo = new Tipo();
        tipo.setId_tipo(1);
        tipo.setTipo(TipoAvion.PASAJERO);
        tipo.setAviones(List.of());
        return tipo;
    }

    // aca parten los test

    @Test
    public void testObtenerTodos() {
        Tipo tipo = createTipo();
        when(tipoRepository.findAll()).thenReturn(List.of(tipo));

        var resultado = tipoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("PASAJERO", resultado.get(0).getTipo());
    }

    @Test
    public void testBuscarPorId() {
        Tipo tipo = createTipo();
        when(tipoRepository.findById(1)).thenReturn(Optional.of(tipo));

        var resultado = tipoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("PASAJERO", resultado.getTipo());
        verify(tipoRepository, times(1)).findById(1);
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(tipoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> tipoService.buscarPorId(99));
    }

    @Test
    public void testGuardarTipo() {
        Tipo tipo = createTipo();
        when(tipoRepository.save(tipo)).thenReturn(tipo);

        var resultado = tipoService.guardarTipo(tipo);

        assertNotNull(resultado);
        assertEquals("PASAJERO", resultado.getTipo());
        verify(tipoRepository, times(1)).save(tipo);
    }

    @Test
    public void testActualizarTipo() {
        Tipo existente = createTipo();
        existente.setTipo(TipoAvion.GUERRA);

        Tipo actualizado = new Tipo();
        actualizado.setTipo(TipoAvion.GUERRA);
        actualizado.setAviones(List.of());

        when(tipoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(tipoRepository.save(any(Tipo.class))).thenReturn(existente);

        var resultado = tipoService.actualizarTipo(1, actualizado);

        assertNotNull(resultado);
        assertEquals("GUERRA", resultado.getTipo());
        verify(tipoRepository, times(1)).save(any(Tipo.class));
    }

    @Test
    public void testActualizarTipoNoExiste() {
        when(tipoRepository.findById(99)).thenReturn(Optional.empty());
        Tipo actualizado = new Tipo();
        actualizado.setTipo(TipoAvion.GUERRA);

        assertThrows(RuntimeException.class, () -> tipoService.actualizarTipo(99, actualizado));
    }

    @Test
    public void testPatchTipo() {
        Tipo existente = createTipo();
        existente.setTipo(TipoAvion.CARGA);

        Tipo patch = new Tipo();
        patch.setTipo(TipoAvion.CARGA);

        when(tipoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(tipoRepository.save(any(Tipo.class))).thenReturn(existente);

        var resultado = tipoService.patchTipo(1, patch);

        assertNotNull(resultado);
        assertEquals("CARGA", resultado.getTipo());
        verify(tipoRepository, times(1)).save(any(Tipo.class));
    }

    @Test
    public void testPatchTipoNoExiste() {
        when(tipoRepository.findById(99)).thenReturn(Optional.empty());
        Tipo patch = new Tipo();
        patch.setTipo(TipoAvion.CARGA);

        assertThrows(RuntimeException.class, () -> tipoService.patchTipo(99, patch));
    }

    @Test
    public void testEliminar() {
        Tipo tipo = createTipo();
        when(tipoRepository.findById(1)).thenReturn(Optional.of(tipo));
        doNothing().when(tipoRepository).delete(tipo);

        String resultado = tipoService.eliminar(1);

        assertTrue(resultado.contains("exitosamente"));
        verify(tipoRepository, times(1)).delete(tipo);
    }

    @Test
    public void testEliminarNoExiste() {
        when(tipoRepository.findById(99)).thenReturn(Optional.empty());

        String resultado = tipoService.eliminar(99);

        assertTrue(resultado.contains("no existe"));
    }
}