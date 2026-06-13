package com.fullstack.SoftwareAviones.msubicacion.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.SoftwareAviones.msubicacion.DTO.RegionDTO;
import com.fullstack.SoftwareAviones.msubicacion.model.Region;
import com.fullstack.SoftwareAviones.msubicacion.model.Comuna;
import com.fullstack.SoftwareAviones.msubicacion.repository.RegionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public List<RegionDTO> obtenerTodos() {
        return regionRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public RegionDTO buscarPorId(Integer id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Se Pudo Encontrar La Region"));
        return convertirADTO(region);
    }

    public String eliminar(Integer id) {
        try {
            Region region = regionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("La región con ID " + id + " no existe"));
            regionRepository.delete(region);
            return "La región '" + region.getRegion() + "' se elimino correctamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public RegionDTO guardarRegion(Region region) {
        return convertirADTO(regionRepository.save(region));
    }

    public RegionDTO actualizarRegion(Integer id, Region regionActualizada) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Región no encontrada"));
        region.setRegion(regionActualizada.getRegion());
        return convertirADTO(regionRepository.save(region));
    }

    private RegionDTO convertirADTO(Region region) {
        RegionDTO dto = new RegionDTO();

        dto.setID_region(region.getID_region());

        dto.setRegion(region.getRegion());

        List<String> nombresComunas = new ArrayList<>();

        if (region.getComunas() != null) {
            for (Comuna comuna : region.getComunas()) {
                nombresComunas.add(comuna.getComuna());
            }
        }
        dto.setComunas(nombresComunas);
        return dto;
        
    }

    public RegionDTO patchRegion(Integer id, Region region) {
        Region region2 = regionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Region no encontrada"));
        if (region.getRegion() != null) {
            region2.setRegion(region.getRegion());
        }
        return convertirADTO(regionRepository.save(region2));
    }
}
