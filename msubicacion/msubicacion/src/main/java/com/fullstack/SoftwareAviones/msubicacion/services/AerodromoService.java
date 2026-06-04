package com.fullstack.SoftwareAviones.msubicacion.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.SoftwareAviones.msubicacion.DTO.AerodromoDTO;
import com.fullstack.SoftwareAviones.msubicacion.model.Aerodromo;
import com.fullstack.SoftwareAviones.msubicacion.repository.AerodromoRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class AerodromoService {

    @Autowired
    private AerodromoRepository aerodromoRepository;


    public List<AerodromoDTO> obtenerTodos() {
        return aerodromoRepository.findAll().stream()
            .map(this::convertirADTO)
            .toList();
    }
    //DTO
    public AerodromoDTO buscarPorId(Integer id) {
        Aerodromo aerodromo = aerodromoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡aerodromo no encontrado!"));
        return convertirADTO(aerodromo);
    }

    // guardar
    public Aerodromo guardarAerodromo(Aerodromo aerodromo) {
        return aerodromoRepository.save(aerodromo);
    }

    //actualizar 
    public Aerodromo actualizarAerodromo(Integer id, Aerodromo aerodromoActualizado) {
        Aerodromo aerodromo = aerodromoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aerodromo no encontrado"));

        
        aerodromo.setNombre_aerodromo(aerodromoActualizado.getNombre_aerodromo());
        aerodromo.setComuna(aerodromoActualizado.getComuna());
            return aerodromoRepository.save(aerodromo);
    }

    //eliminar 
    public String eliminar(Integer id) {
        try {
            Aerodromo aerodromo = aerodromoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! El Aerodromo con ID " + id + " no existe."));
            aerodromoRepository.delete(aerodromo);
            return "El Aerodromo '" + aerodromo.getNombre_aerodromo() + "' ha sido retirado exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
        
    // DTO
    private AerodromoDTO convertirADTO(Aerodromo aerodromo) {
        AerodromoDTO dto = new AerodromoDTO();

        dto.setID_aerodromo(aerodromo.getID_aerodromo());
        dto.setNombre_aerodromo(aerodromo.getNombre_aerodromo());


        if (aerodromo.getComuna() != null) {
            dto.setComuna(
                    aerodromo.getComuna().getComuna()
            );
        }
        return dto;
    }

    public Aerodromo patchAerodromo(Integer id, Aerodromo aerodromo) {
        Aerodromo aerodromo2 = aerodromoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aerodromo no encontrado"));
        if (aerodromo.getNombre_aerodromo() != null) {
            aerodromo2.setNombre_aerodromo(aerodromo.getNombre_aerodromo());
        }
        if (aerodromo.getComuna() != null) {
            aerodromo2.setComuna(aerodromo.getComuna());
        }
        return aerodromoRepository.save(aerodromo2);
    }

}

