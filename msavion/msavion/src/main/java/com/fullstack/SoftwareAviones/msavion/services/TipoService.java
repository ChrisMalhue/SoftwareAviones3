package com.fullstack.SoftwareAviones.msavion.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.SoftwareAviones.msavion.DTO.TipoDTO;

import com.fullstack.SoftwareAviones.msavion.model.Tipo;
import com.fullstack.SoftwareAviones.msavion.repository.TipoRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class TipoService {

    @Autowired
    private TipoRepository tipoRepository;

    public List<TipoDTO> obtenerTodos() {
        log.info("Obteniendo Todos los Datos De Tipos");
        return tipoRepository.findAll().stream()
            .map(this::convertirADTO)
            .toList();
    }

    public TipoDTO buscarPorId(Integer id) {
        log.info("Buscando Tipo Por ID: {}", id);
        Tipo tipo = tipoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Tipo no encontrado!"));
        return convertirADTO(tipo);
    }

    public TipoDTO guardarTipo(Tipo tipo) {
        log.info("Registrando Nuevo Tipo");
        return convertirADTO(tipoRepository.save(tipo));
    }

    public Tipo buscarID_Tipo(Integer id){
        log.info("Buscando Tipo Por ID: {}", id);
        return tipoRepository.findById(id).orElse(null);
    }
    //actualizar 
    public TipoDTO actualizarTipo(Integer id, Tipo tipoActualizado) {
        log.info("Actualizando Informacion Del Tipo con ID: {}", id);
        Tipo tipo = tipoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));
        tipo.setTipo(tipoActualizado.getTipo());
        return convertirADTO(tipoRepository.save(tipo));
    }
    //eliminar 
    public String eliminar(Integer id) {
        log.info("Eliminando Un Tipo Del Sistema");
        try {
            Tipo tipo = tipoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! El Tipo con ID " + id + " no existe."));
            tipoRepository.delete(tipo);
            return "El Tipo'" + tipo.getId_tipo() + "' ha sido retirado exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private TipoDTO convertirADTO(Tipo tipo) {

        TipoDTO dto = new TipoDTO();
        
        dto.setId_tipo(tipo.getId_tipo());

        if (tipo.getTipo() != null) {
            dto.setTipo(tipo.getTipo().name());
        }
        List<String> aviones = new ArrayList<>();

        if (tipo.getAviones() != null) {
            tipo.getAviones().forEach(avion -> {
                aviones.add(avion.getMatricula());
            });
        }

        dto.setAviones(aviones);


        return dto;
    }

    public TipoDTO patchTipo(Integer id, Tipo tipo) {
        Tipo tipo2 = tipoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));
        if (tipo.getTipo() != null) {
            tipo2.setTipo(tipo.getTipo());
        }
        return convertirADTO(tipoRepository.save(tipo2));
    }
}

