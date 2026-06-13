package com.fullstack.SoftwareAviones.msubicacion.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.SoftwareAviones.msubicacion.DTO.ComunaDTO;
import com.fullstack.SoftwareAviones.msubicacion.model.Aerodromo;
import com.fullstack.SoftwareAviones.msubicacion.model.Comuna;
import com.fullstack.SoftwareAviones.msubicacion.repository.ComunaRepository;
import com.fullstack.SoftwareAviones.msubicacion.repository.RegionRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private RegionRepository regionRepository;

    public List<ComunaDTO> obtenerTodos(){
        return comunaRepository.findAll().stream()
            .map(this::convertirADTO)
            .toList();
    }

    public ComunaDTO buscarPorId(Integer id) {
        Comuna comuna = comunaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Comuna no encontrada!"));
        return convertirADTO(comuna);
    }

    public String eliminar(Integer id) {
        try {
            Comuna comuna = comunaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! La Comuna con ID " + id + " no existe."));
            comunaRepository.delete(comuna);
            return "La Comuna'" + comuna.getComuna() + "' ha sido retirado exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public ComunaDTO guardarComuna(Comuna comuna) {
        comuna.setRegion(regionRepository.findById(comuna.getRegion().getID_region())
            .orElseThrow(() -> new RuntimeException("Región no encontrada")));
        return convertirADTO(comunaRepository.save(comuna));
    }


    public ComunaDTO actualizarComuna(Integer id, Comuna comunaActualizado) {
        Comuna comuna = comunaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada"));

        comuna.setComuna(comunaActualizado.getComuna());

        comuna.setRegion(regionRepository.findById(comunaActualizado.getRegion().getID_region())
            .orElseThrow(() -> new RuntimeException("Región no encontrada")));

        return convertirADTO(comunaRepository.save(comuna));
    }

    private ComunaDTO convertirADTO(Comuna comuna) {
        ComunaDTO dto = new ComunaDTO();

        dto.setID_comuna(comuna.getID_comuna());
        dto.setComuna(comuna.getComuna());

        List<String> nombresAerodromos = new ArrayList<>();
        if (comuna.getAerodromos() != null) {
            for (Aerodromo aerodromo : comuna.getAerodromos()) {
                nombresAerodromos.add(
                    aerodromo.getNombre_aerodromo()
                );
            }
        }

        dto.setAerodromos(nombresAerodromos);

        if (comuna.getRegion() != null) {
            dto.setRegion(
                comuna.getRegion().getRegion()
            );
        }

        return dto;
    }

    public ComunaDTO patchComuna(Integer id, Comuna comuna) {
        Comuna comuna2 = comunaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comuna no encontrada"));
        if (comuna.getComuna() != null) {
            comuna2.setComuna(comuna.getComuna());
        }
        if (comuna.getRegion() != null) {
            comuna2.setRegion(regionRepository.findById(comuna.getRegion().getID_region())
                .orElseThrow(() -> new RuntimeException("Región no encontrada")));
        }
        return convertirADTO(comunaRepository.save(comuna2));
    }
}

