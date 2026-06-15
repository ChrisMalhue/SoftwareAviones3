package com.fullstack.SoftwareAviones.mspiloto.services;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.SoftwareAviones.mspiloto.DTO.PilotoDTO;
import com.fullstack.SoftwareAviones.mspiloto.model.Cursos;
import com.fullstack.SoftwareAviones.mspiloto.model.Piloto;
import com.fullstack.SoftwareAviones.mspiloto.repository.PilotoRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PilotoService {

    @Autowired
    private PilotoRepository pilotoRepository;

    public List<PilotoDTO> obtenerTodos() {
        log.info("Obteniendo Todos los Datos De Pilotos");
        return pilotoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public PilotoDTO buscarPorId(Integer id) {
        log.info("Buscando Piloto Por ID: {}", id);
        Piloto piloto = pilotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡Piloto no encontrado!"));
        return convertirADTO(piloto);
    }

    public String eliminar(Integer id) {
        log.info("Eliminando Un Piloto Del Sistema");
        try {
            Piloto piloto = pilotoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! El piloto con ID " + id + " no existe."));
            pilotoRepository.delete(piloto);
            return "El piloto '" + piloto.getNombre() + "' ha sido retirado exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public PilotoDTO guardarPiloto(Piloto piloto) {
        log.info("Registrando Un Nuevo Piloto");
        return convertirADTO(pilotoRepository.save(piloto));
    }

    public PilotoDTO actualizarPiloto(Integer id, Piloto pilotoActualizado) {
        log.info("Actualizando Informacion Del Piloto con ID: {}", id);
        Piloto piloto = pilotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Piloto no encontrado"));

        piloto.setRut(pilotoActualizado.getRut());
        piloto.setNombre(pilotoActualizado.getNombre());
        piloto.setApellido(pilotoActualizado.getApellido());
        piloto.setFecha_nacimiento(pilotoActualizado.getFecha_nacimiento());
        piloto.setHoras_vuelo(pilotoActualizado.getHoras_vuelo());

        return convertirADTO(pilotoRepository.save(piloto));
    }

    private PilotoDTO convertirADTO(Piloto piloto) {
        PilotoDTO dto = new PilotoDTO();

        dto.setID_piloto(piloto.getID_piloto());
        dto.setRut(piloto.getRut());
        dto.setNombre(piloto.getNombre());
        dto.setApellido(piloto.getApellido());
        dto.setFecha_nacimiento(piloto.getFecha_nacimiento());
        dto.setHoras_vuelo(piloto.getHoras_vuelo());

        List<String> nombresCursos = new ArrayList<>();
        if (piloto.getCursosAprendidos() != null) {
            for (Cursos cursoPiloto : piloto.getCursosAprendidos()) {
                nombresCursos.add(
                    cursoPiloto.getCurso().getNombre_curso()
                );
            }
        }

        dto.setCursosAprendidos(nombresCursos);

        return dto;
    }

    public List<PilotoDTO> buscarPorNombre(String nombre){
        return pilotoRepository.buscarPorNombre(nombre).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<PilotoDTO> buscarPilotosConHorasMinimas(Integer horasMinimas){
        return pilotoRepository.buscarPilotosConHorasMinimas(horasMinimas).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public PilotoDTO patchPiloto(Integer id, Piloto piloto) {
        Piloto piloto2 = pilotoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Piloto no encontrado"));
        if (piloto.getRut() != null)
            piloto2.setRut(piloto.getRut());
        if (piloto.getNombre() != null)
            piloto2.setNombre(piloto.getNombre());
        if (piloto.getApellido() != null)
            piloto2.setApellido(piloto.getApellido());
        if (piloto.getFecha_nacimiento() != null)
            piloto2.setFecha_nacimiento(piloto.getFecha_nacimiento());
        if (piloto.getHoras_vuelo() != null)
            piloto2.setHoras_vuelo(piloto.getHoras_vuelo());
        return convertirADTO(pilotoRepository.save(piloto2));
    }
    
}
