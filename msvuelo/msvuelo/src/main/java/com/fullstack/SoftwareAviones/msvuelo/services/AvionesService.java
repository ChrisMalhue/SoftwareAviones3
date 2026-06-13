package com.fullstack.SoftwareAviones.msvuelo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fullstack.SoftwareAviones.msvuelo.DTO.AvionDTO;
import com.fullstack.SoftwareAviones.msvuelo.DTO.AvionesDTO;
import com.fullstack.SoftwareAviones.msvuelo.DTO.PilotoDTO;
import com.fullstack.SoftwareAviones.msvuelo.model.Aviones;
import com.fullstack.SoftwareAviones.msvuelo.repository.AvionesRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AvionesService {

    @Autowired
    private AvionesRepository avionesRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.piloto.url}")
    private String pilotoUrl;

    @Value("${services.avion.url}")
    private String avionUrl;

    public List<AvionesDTO> obtenerTodos() {
        return avionesRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public AvionesDTO buscarPorId(Integer id) {
        Aviones aviones = avionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡Registro no encontrado!"));
        return convertirADTO(aviones);
    }

    public AvionesDTO guardarAviones(Aviones aviones) {
        return convertirADTO(avionesRepository.save(aviones));
    }

    public AvionesDTO actualizarAviones(Integer id, Aviones avionesActualizado) {
        Aviones aviones = avionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        aviones.setIdPiloto(avionesActualizado.getIdPiloto());
        aviones.setIdAvion(avionesActualizado.getIdAvion());
        return convertirADTO(avionesRepository.save(aviones));
    }

    public String eliminar(Integer id) {
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

        // Llamada a mspiloto
        try {
            PilotoDTO piloto = restTemplate.getForObject(
                pilotoUrl + "/api/v1/pilotos/" + aviones.getIdPiloto(),
                PilotoDTO.class
            );
            if (piloto != null) {
                dto.setNombrePiloto(piloto.getNombre());
            }
        } catch (Exception e) {
            dto.setNombrePiloto("Piloto no disponible");
        }

        // Llamada a msavion 
        try {
            AvionDTO avion = restTemplate.getForObject(
                avionUrl + "/api/v1/aviones/" + aviones.getIdAvion(),
                AvionDTO.class
            );
            if (avion != null) {
                dto.setModeloAvion(avion.getModelo());
            }
        } catch (Exception e) {
            dto.setModeloAvion("Avion no disponible");
        }

        return dto;
    }
}