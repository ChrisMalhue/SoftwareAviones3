package com.fullstack.SoftwareAviones.msvuelo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fullstack.SoftwareAviones.msvuelo.DTO.VueloDTO;
import com.fullstack.SoftwareAviones.msvuelo.DTO.PilotoDTO;
import com.fullstack.SoftwareAviones.msvuelo.DTO.AvionDTO;
import com.fullstack.SoftwareAviones.msvuelo.DTO.AerodromoDTO;
import com.fullstack.SoftwareAviones.msvuelo.model.Vuelo;
import com.fullstack.SoftwareAviones.msvuelo.repository.VueloRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class VueloService {

    @Autowired
    private VueloRepository vueloRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<VueloDTO> obtenerTodos() {
        log.info("Obteniendo Todos los Datos De Vuelos");
        return vueloRepository.findAll().stream()
            .map(this::convertirADTO)
            .toList();
    }

    public VueloDTO buscarPorId(Integer id) {
        log.info("Buscando Su Vuelo Por ID: {}", id);
        Vuelo vuelo = vueloRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Vuelo no encontrado!"));
        return convertirADTO(vuelo);
    }

    public String eliminar(Integer id) {
        log.info("Eliminando Un Vuelo Del Sistema");
        try {
            Vuelo vuelo = vueloRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! El vuelo con ID " + id + " no existe."));
            vueloRepository.delete(vuelo);
            return "El vuelo '" + vuelo.getNumero_vuelo() + "' ha sido retirado exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public VueloDTO agregarVuelo(Vuelo vuelo) {
        log.info("Registrando Nuevo Vuelo");
        return convertirADTO(vueloRepository.save(vuelo));
    }

    public VueloDTO actualizarVuelo(Integer id, Vuelo vueloActualizado) {
        log.info("Actualizando Informacion Del Vuelo con ID: {}", id);
        Vuelo vuelo = vueloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vuelo no encontrado"));

        vuelo.setNumero_vuelo(vueloActualizado.getNumero_vuelo());
        vuelo.setHora_inicio_vuelo(vueloActualizado.getHora_inicio_vuelo());
        vuelo.setTipo_vuelo(vueloActualizado.getTipo_vuelo());
        vuelo.setDestino(vueloActualizado.getDestino());
        vuelo.setIdPiloto(vueloActualizado.getIdPiloto());
        vuelo.setIdAvion(vueloActualizado.getIdAvion());
        vuelo.setIdAerodromo(vueloActualizado.getIdAerodromo());

        return convertirADTO(vueloRepository.save(vuelo));
    }

    public VueloDTO patchVuelo(Integer id, Vuelo vuelo) {
        Vuelo vuelo2 = vueloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vuelo no encontrado"));
        if (vuelo.getNumero_vuelo() != null)
            vuelo2.setNumero_vuelo(vuelo.getNumero_vuelo());
        if (vuelo.getHora_inicio_vuelo() != null)
            vuelo2.setHora_inicio_vuelo(vuelo.getHora_inicio_vuelo());
        if (vuelo.getTipo_vuelo() != null)
            vuelo2.setTipo_vuelo(vuelo.getTipo_vuelo());
        if (vuelo.getDestino() != null)
            vuelo2.setDestino(vuelo.getDestino());
        if (vuelo.getIdPiloto() != null)
            vuelo2.setIdPiloto(vuelo.getIdPiloto());
        if (vuelo.getIdAvion() != null)
            vuelo2.setIdAvion(vuelo.getIdAvion());
        if (vuelo.getIdAerodromo() != null)
            vuelo2.setIdAerodromo(vuelo.getIdAerodromo());
        return convertirADTO(vueloRepository.save(vuelo2));
    }

    private VueloDTO convertirADTO(Vuelo vuelo) {
        VueloDTO dto = new VueloDTO();
        dto.setID_vuelo(vuelo.getID_vuelo());
        dto.setNumero_vuelo(vuelo.getNumero_vuelo());
        dto.setHora_inicio_vuelo(vuelo.getHora_inicio_vuelo());
        dto.setTipo_vuelo(vuelo.getTipo_vuelo());
        dto.setDestino(vuelo.getDestino());
        dto.setIdPiloto(vuelo.getIdPiloto());
        dto.setIdAvion(vuelo.getIdAvion());
        dto.setIdAerodromo(vuelo.getIdAerodromo());

        // llamada a ms-piloto
        try {
            PilotoDTO piloto = webClientBuilder.build()
                .get()
                .uri("http://mspiloto/api/v1/pilotos/" + vuelo.getIdPiloto())
                .retrieve()
                .bodyToMono(PilotoDTO.class)
                .block();
            if (piloto != null) dto.setNombrePiloto(piloto.getNombre());
        } catch (Exception e) {
            dto.setNombrePiloto("Piloto no disponible");
        }

        // llamada a ms-avion
        try {
            AvionDTO avion = webClientBuilder.build()
                .get()
                .uri("http://msavion/api/v1/aviones/" + vuelo.getIdAvion())
                .retrieve()
                .bodyToMono(AvionDTO.class)
                .block();
            if (avion != null) dto.setModeloAvion(avion.getModelo());
        } catch (Exception e) {
            dto.setModeloAvion("Avion no disponible");
        }

        // llamada a ms-ubicacion
        try {
            AerodromoDTO aerodromo = webClientBuilder.build()
                .get()
                .uri("http://msubicacion/api/v1/aerodromos/" + vuelo.getIdAerodromo())
                .retrieve()
                .bodyToMono(AerodromoDTO.class)
                .block();
            if (aerodromo != null) dto.setNombreAerodromo(aerodromo.getNombre_aerodromo());
        } catch (Exception e) {
            dto.setNombreAerodromo("Aerodromo no disponible");
        }

        return dto;
    }
}