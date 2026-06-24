package com.fullstack.SoftwareAviones.mspiloto.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import net.datafaker.Faker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.fullstack.SoftwareAviones.mspiloto.model.Piloto;
import com.fullstack.SoftwareAviones.mspiloto.repository.PilotoRepository;

@SpringBootTest
public class PilotoServiceTest {

    @Autowired
    private PilotoService pilotoService;

    @MockitoBean
    private PilotoRepository pilotoRepository;

    private final Faker faker = new Faker();

    // el creador

    private Piloto createPiloto() {
        Piloto piloto = new Piloto();
        piloto.setID_piloto(1);
        piloto.setRut(faker.regexify("[0-9]{2}\\.[0-9]{3}\\.[0-9]{3}-[0-9]"));
        piloto.setNombre(faker.regexify("[A-Za-z]{3,10}"));
        piloto.setApellido(faker.regexify("[A-Za-z]{3,10}"));
        piloto.setFecha_nacimiento(new Date(90, 1, 1));
        piloto.setHoras_vuelo(faker.number().numberBetween(0, 5000));
        piloto.setCursosAprendidos(List.of());
        return piloto;
    }

    // aca parten los test

    @Test
    public void testObtenerTodos() {
        Piloto piloto = createPiloto();
        when(pilotoRepository.findAll()).thenReturn(List.of(piloto));

        var resultado = pilotoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(piloto.getNombre(), resultado.get(0).getNombre());
    }

    @Test
    public void testBuscarPorId() {
        Piloto piloto = createPiloto();
        when(pilotoRepository.findById(1)).thenReturn(Optional.of(piloto));

        var resultado = pilotoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(piloto.getNombre(), resultado.getNombre());
        verify(pilotoRepository, times(1)).findById(1);
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(pilotoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pilotoService.buscarPorId(99));
    }

    @Test
    public void testGuardarPiloto() {
        Piloto piloto = createPiloto();
        when(pilotoRepository.save(piloto)).thenReturn(piloto);

        var resultado = pilotoService.guardarPiloto(piloto);

        assertNotNull(resultado);
        assertEquals(piloto.getNombre(), resultado.getNombre());
        verify(pilotoRepository, times(1)).save(piloto);
    }

    @Test
    public void testActualizarPiloto() {
        Piloto existente = createPiloto();
        String nuevoNombre = faker.regexify("[A-Za-z]{3,10}");
        existente.setNombre(nuevoNombre); // el service muta existente antes del save

        Piloto actualizado = createPiloto();
        actualizado.setNombre(nuevoNombre);

        when(pilotoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(pilotoRepository.save(any(Piloto.class))).thenReturn(actualizado);

        var resultado = pilotoService.actualizarPiloto(1, actualizado);

        assertNotNull(resultado);
        assertEquals(nuevoNombre, resultado.getNombre());
        verify(pilotoRepository, times(1)).save(any(Piloto.class));
    }

    @Test
    public void testActualizarPilotoNoExiste() {
        when(pilotoRepository.findById(99)).thenReturn(Optional.empty());
        Piloto actualizado = createPiloto();

        assertThrows(RuntimeException.class, () -> pilotoService.actualizarPiloto(99, actualizado));
    }

    @Test
    public void testPatchPiloto() {
        Piloto existente = createPiloto();
        String nuevoNombre = faker.regexify("[A-Za-z]{3,10}");
        existente.setNombre(nuevoNombre); // patch muta existente antes del save

        Piloto patch = new Piloto();
        patch.setNombre(nuevoNombre);

        when(pilotoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(pilotoRepository.save(any(Piloto.class))).thenReturn(existente);

        var resultado = pilotoService.patchPiloto(1, patch);

        assertNotNull(resultado);
        assertEquals(nuevoNombre, resultado.getNombre());
        verify(pilotoRepository, times(1)).save(any(Piloto.class));
    }

    @Test
    public void testPatchPilotoNoExiste() {
        when(pilotoRepository.findById(99)).thenReturn(Optional.empty());
        Piloto patch = new Piloto();
        patch.setNombre(faker.regexify("[A-Za-z]{3,10}"));

        assertThrows(RuntimeException.class, () -> pilotoService.patchPiloto(99, patch));
    }

    @Test
    public void testEliminar() {
        Piloto piloto = createPiloto();
        when(pilotoRepository.findById(1)).thenReturn(Optional.of(piloto));
        doNothing().when(pilotoRepository).delete(piloto);

        String resultado = pilotoService.eliminar(1);

        assertTrue(resultado.contains("exitosamente"));
        verify(pilotoRepository, times(1)).delete(piloto);
    }

    @Test
    public void testEliminarNoExiste() {
        when(pilotoRepository.findById(99)).thenReturn(Optional.empty());

        String resultado = pilotoService.eliminar(99);

        assertTrue(resultado.contains("no existe"));
    }

    @Test
    public void testBuscarPorNombre() {
        Piloto piloto = createPiloto();
        String nombre = piloto.getNombre();
        when(pilotoRepository.buscarPorNombre(nombre)).thenReturn(List.of(piloto));

        var resultado = pilotoService.buscarPorNombre(nombre);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(nombre, resultado.get(0).getNombre());
    }

    @Test
    public void testBuscarPorNombreNoExiste() {
        String nombreInexistente = faker.regexify("[A-Za-z]{3,10}");
        when(pilotoRepository.buscarPorNombre(nombreInexistente)).thenReturn(List.of());

        var resultado = pilotoService.buscarPorNombre(nombreInexistente);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPilotosConHorasMinimas() {
        Piloto piloto = createPiloto();
        int horasMinimas = piloto.getHoras_vuelo() - 1;
        when(pilotoRepository.buscarPilotosConHorasMinimas(horasMinimas)).thenReturn(List.of(piloto));

        var resultado = pilotoService.buscarPilotosConHorasMinimas(horasMinimas);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(piloto.getHoras_vuelo(), resultado.get(0).getHoras_vuelo());
    }

    @Test
    public void testBuscarPilotosConHorasMinimasNoExiste() {
        when(pilotoRepository.buscarPilotosConHorasMinimas(9999)).thenReturn(List.of());

        var resultado = pilotoService.buscarPilotosConHorasMinimas(9999);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}