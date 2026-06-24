package com.fullstack.SoftwareAviones.msvuelo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fullstack.SoftwareAviones.msvuelo.DTO.AvionDTO;
import com.fullstack.SoftwareAviones.msvuelo.DTO.AvionesDTO;
import com.fullstack.SoftwareAviones.msvuelo.DTO.PilotoDTO;
import com.fullstack.SoftwareAviones.msvuelo.model.Aviones;
import com.fullstack.SoftwareAviones.msvuelo.repository.AvionesRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class AvionesService {

    @Autowired
    private AvionesRepository avionesRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<AvionesDTO> obtenerTodos() {
        log.info("Obteniendo Todos los Datos De Aviones");
        return avionesRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public AvionesDTO buscarPorId(Integer id) {
        log.info("Buscando El Avion Por ID: {}", id);
        Aviones aviones = avionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡Registro no encontrado!"));
        return convertirADTO(aviones);
    }

    public AvionesDTO guardarAviones(Aviones aviones) {
        log.info("Registrando Nuevo Avion");
        return convertirADTO(avionesRepository.save(aviones));
    }

    public AvionesDTO actualizarAviones(Integer id, Aviones avionesActualizado) {
        log.info("Actualizando Informacion Del Avion con ID: {}", id);
        Aviones aviones = avionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        aviones.setIdPiloto(avionesActualizado.getIdPiloto());
        aviones.setIdAvion(avionesActualizado.getIdAvion());
        return convertirADTO(avionesRepository.save(aviones));
    }

    public String eliminar(Integer id) {
        log.info("Eliminando Un Avion Del Sistema");
        try {
            Aviones aviones = avionesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! El registro con ID " + id + " no existe."));
            avionesRepository.delete(aviones);
            return "El registro fue eliminado correctamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private AvionesDTO convertirADTO(Aviones aviones) {
        AvionesDTO dto = new AvionesDTO();
        dto.setID_aviones(aviones.getID_aviones());
        dto.setIdPiloto(aviones.getIdPiloto());
        dto.setIdAvion(aviones.getIdAvion());

        // llamada a mspiloto
        try {
            PilotoDTO piloto = webClientBuilder.build()
                .get()
                .uri("http://mspiloto/api/v1/pilotos/" + aviones.getIdPiloto())
                .retrieve()
                .bodyToMono(PilotoDTO.class)
                .block();
            if (piloto != null) dto.setNombrePiloto(piloto.getNombre());
        } catch (Exception e) {
            dto.setNombrePiloto("Piloto no disponible");
        }

        // llamada a msavion
        try {
            AvionDTO avion = webClientBuilder.build()
                .get()
                .uri("http://msavion/api/v1/aviones/" + aviones.getIdAvion())
                .retrieve()
                .bodyToMono(AvionDTO.class)
                .block();
            if (avion != null) dto.setModeloAvion(avion.getModelo());
        } catch (Exception e) {
            dto.setModeloAvion("Avion no disponible");
        }

        return dto;
    }
}