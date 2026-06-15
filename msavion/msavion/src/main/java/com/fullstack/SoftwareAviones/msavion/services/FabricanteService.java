package com.fullstack.SoftwareAviones.msavion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.SoftwareAviones.msavion.DTO.FabricanteDTO;
import com.fullstack.SoftwareAviones.msavion.model.Avion;
import com.fullstack.SoftwareAviones.msavion.model.Fabricante;
import com.fullstack.SoftwareAviones.msavion.repository.FabricanteRepository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class FabricanteService {

    @Autowired
    private FabricanteRepository fabricanteRepository;

    public List<FabricanteDTO> obtenerTodos() {
        log.info("Obteniendo Todos los Datos De Fabricantes");
        return fabricanteRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }
    
    public FabricanteDTO buscarPorId(Integer id) {
        log.info("Buscando Fabricante Por ID: {}", id);
        Fabricante fabricante = fabricanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡Fabricante no encontrado!"));

        return convertirADTO(fabricante);
    }
    
    public FabricanteDTO guardarFabricante(Fabricante fabricante) {
        log.info("Registrando Nuevo Fabricante");
        return convertirADTO(fabricanteRepository.save(fabricante));
    }
    
    public FabricanteDTO actualizarFabricante(Integer id, Fabricante fabricanteActualizado) {
        log.info("Actualizando Informacion Del Fabricante con ID: {}", id);
        Fabricante fabricante = fabricanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fabricante no encontrado"));
        fabricante.setNombre_fabricante(fabricanteActualizado.getNombre_fabricante());
        return convertirADTO(fabricanteRepository.save(fabricante));
    }

    public String eliminar(Integer id) {
        log.info("Eliminando Un Fabricante Del Sistema");
        try {
            Fabricante fabricante = fabricanteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException(
                            "¡Imposible eliminar! El fabricante con ID " + id + " no existe."
                    ));
            fabricanteRepository.delete(fabricante);
            return "El fabricante '" + fabricante.getNombre_fabricante() + "' fue eliminado correctamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private FabricanteDTO convertirADTO(Fabricante fabricante) {

        FabricanteDTO dto = new FabricanteDTO();

        dto.setId_fabricante(fabricante.getId_fabricante());
        dto.setNombre_fabricante(fabricante.getNombre_fabricante());

        List<String> aviones = new ArrayList<>();

        if (fabricante.getAviones() != null) {
            for (Avion avion : fabricante.getAviones()) {
                aviones.add(avion.getMatricula());
            }
        }

        dto.setAviones(aviones);
        return dto;
    }

    public FabricanteDTO patchFabricante(Integer id, Fabricante fabricante) {
        log.info("Actualizando Informacion Del Fabricante con ID: {}", id);
        Fabricante fabricante2 = fabricanteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fabricante no encontrado"));
        if (fabricante.getNombre_fabricante() != null) {
            fabricante2.setNombre_fabricante(fabricante.getNombre_fabricante());
        }
        return convertirADTO(fabricanteRepository.save(fabricante2));
    }
}
