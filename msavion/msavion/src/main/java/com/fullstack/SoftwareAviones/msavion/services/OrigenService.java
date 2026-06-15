package com.fullstack.SoftwareAviones.msavion.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.SoftwareAviones.msavion.DTO.OrigenDTO;
import com.fullstack.SoftwareAviones.msavion.model.Avion;
import com.fullstack.SoftwareAviones.msavion.model.Origen;
import com.fullstack.SoftwareAviones.msavion.repository.OrigenRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class OrigenService {

    @Autowired
    private OrigenRepository origenRepository;

    public List<OrigenDTO> obtenerTodos() {
        log.info("Obteniendo Todos los Datos De Orígenes");
        return origenRepository.findAll().stream()
        .map(this::convertirADTO)
        .toList();
    }

    public OrigenDTO buscarPorId(Integer id) {
        log.info("Buscando Origen Por ID: {}", id);
        Origen origen = origenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Origen no encontrado!"));
        return convertirADTO(origen);
    }
    
    public OrigenDTO guardarOrigen(Origen origen){
        log.info("Registrando Nuevo Origen");
        return convertirADTO(origenRepository.save(origen));
    }

    //actualizar 
    public OrigenDTO actualizarOrigen(Integer id, Origen origenActualizado) {
        log.info("Actualizando Informacion Del Origen con ID: {}", id);
        Origen origen = origenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Origen no encontrado"));
        origen.setPais_origen(origenActualizado.getPais_origen());
        return convertirADTO(origenRepository.save(origen));
    }

    //eliminar 
    public String eliminar(Integer id) {
        log.info("Eliminando Un Origen Del Sistema");
        try {
            Origen origen = origenRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! El Origen con ID " + id + " no existe."));
            origenRepository.delete(origen);
            return "El Origen '" + origen.getId_origen() + "' ha sido retirado exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private OrigenDTO convertirADTO(Origen origen) {
        OrigenDTO dto = new OrigenDTO();

        dto.setId_origen(origen.getId_origen());
        dto.setPais_origen(origen.getPais_origen());

        List<String> aviones = new ArrayList<>();

        if (origen.getAviones() != null) {
            for (Avion avion : origen.getAviones()) {
                aviones.add(avion.getMatricula());
            }
        }

        dto.setAviones(aviones);
        return dto;
    }

    public OrigenDTO patchOrigen(Integer id, Origen origen) {
        log.info("Actualizando Informacion Del Origen con ID: {}", id);
        Origen origen2 = origenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Origen no encontrado"));
        if (origen.getPais_origen() != null) {
            origen2.setPais_origen(origen.getPais_origen());
        }
        return convertirADTO(origenRepository.save(origen2));
    }
}
