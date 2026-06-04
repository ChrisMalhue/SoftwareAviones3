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

@Service
@Transactional
public class FabricanteService {

    @Autowired
    private FabricanteRepository fabricanteRepository;

    public List<FabricanteDTO> obtenerTodos() {
        return fabricanteRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }
    
    public FabricanteDTO buscarPorId(Integer id) {
        Fabricante fabricante = fabricanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡Fabricante no encontrado!"));

        return convertirADTO(fabricante);
    }
    
    public Fabricante guardarFabricante(Fabricante fabricante) {
        return fabricanteRepository.save(fabricante);
    }
    
    public Fabricante actualizarFabricante(Integer id, Fabricante fabricanteActualizado) {
        Fabricante fabricante = fabricanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fabricante no encontrado"));

        fabricante.setNombre_fabricante(fabricanteActualizado.getNombre_fabricante());
        return fabricanteRepository.save(fabricante);
    }

    public String eliminar(Integer id) {
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

    public Fabricante patchFabricante(Integer id, Fabricante fabricante) {
        Fabricante fabricante2 = fabricanteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fabricante no encontrado"));
        if (fabricante.getNombre_fabricante() != null) {
            fabricante2.setNombre_fabricante(fabricante.getNombre_fabricante());
        }
        return fabricanteRepository.save(fabricante2);
    }



}
