package com.fullstack.SoftwareAviones.msavion.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.SoftwareAviones.msavion.DTO.AvionDTO;
import com.fullstack.SoftwareAviones.msavion.controller.AvionController;
import com.fullstack.SoftwareAviones.msavion.model.Avion;
import com.fullstack.SoftwareAviones.msavion.model.TipoAvion;
import com.fullstack.SoftwareAviones.msavion.repository.AvionRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AvionService {

    @Autowired
    private AvionRepository avionRepository;

    public List<AvionDTO> obtenerTodos() {
        return avionRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public AvionDTO buscarPorId(Integer id) {
        Avion avion = avionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡Avión no encontrado!"));

        return convertirADTO(avion);
    }

    public String eliminar(Integer id) {
        try {
            Avion avion = avionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException(
                            "¡Imposible eliminar! El avión con ID " + id + " no existe."
                    ));
            avionRepository.delete(avion);
            return "El avión con matrícula '" + avion.getMatricula() + "' fue eliminado correctamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private void validarTipo(Avion avion) {

        if (avion.getTipo() == null) {
            return;
        }

        TipoAvion tipo = avion.getTipo().getTipo();

        switch (tipo) {
            case PASAJERO:
                if (avion.getCapacidad_pasajero() == null) {
                    throw new RuntimeException(
                        "Los aviones de pasajeros deben tener capacidad de pasajeros."
                    );
                }
                avion.setCapacidad_carga_kg(null);
                avion.setAlcance_km(null);
                avion.setCantidad_asientos_vip(null);
                break;
            case GUERRA:
                if (avion.getAlcance_km() == null) {
                    throw new RuntimeException(
                            "Los aviones de guerra deben tener alcance."
                    );
                }
                avion.setCapacidad_pasajero(null);
                avion.setCapacidad_carga_kg(null);
                avion.setCantidad_asientos_vip(null);
                break;
            case CARGA:
                if (avion.getCapacidad_carga_kg() == null) {
                    throw new RuntimeException(
                            "Los aviones de carga deben tener capacidad de carga."
                    );
                }
                avion.setCapacidad_pasajero(null);
                avion.setAlcance_km(null);
                avion.setCantidad_asientos_vip(null);
                break;
            case PRIVADO:
                if (avion.getCantidad_asientos_vip() == null) {
                    throw new RuntimeException(
                            "Los aviones privados deben tener asientos VIP."
                    );
                }
                avion.setCapacidad_pasajero(null);
                avion.setCapacidad_carga_kg(null);
                avion.setAlcance_km(null);
                break;
            default:
                throw new RuntimeException("Tipo de avión no válido");
        }
    }

    public Avion guardarAvion(Avion avion) {
        normalizarTipo(avion);
        validarTipo(avion);
        return avionRepository.save(avion);
    }

    private AvionDTO convertirADTO(Avion avion) {
        AvionDTO dto = new AvionDTO();

        dto.setID_avion(avion.getID_avion());
        dto.setMatricula(avion.getMatricula());
        dto.setMarca(avion.getMarca());
        dto.setModelo(avion.getModelo());
        if (avion.getTipo() != null) {
            dto.setTipo(
                    avion.getTipo().getTipo().name()
            );
        }

        dto.setCapacidad_pasajero(avion.getCapacidad_pasajero());
        dto.setCapacidad_carga_kg(avion.getCapacidad_carga_kg());
        dto.setAlcance_km(avion.getAlcance_km());
        dto.setCantidad_asientos_vip(avion.getCantidad_asientos_vip());

        dto.setEnvergadura_metros(avion.getEnvergadura_metros());
        dto.setCapacidad_combustible(avion.getCapacidad_combustible());

        if (avion.getFabricante() != null) {
            dto.setFabricante(
                    avion.getFabricante().getNombre_fabricante()
            );
        }

        if (avion.getOrigen() != null) {
            dto.setOrigen(
                    avion.getOrigen().getPais_origen()
            );
        }

        dto.add(linkTo(methodOn(AvionController.class).buscarPorId(avion.getID_avion())).withSelfRel());
        dto.add(linkTo(methodOn(AvionController.class).obtenerTodos()).withRel("todos"));
        dto.add(linkTo(methodOn(AvionController.class).eliminarAvion(avion.getID_avion())).withRel("eliminar"));
        dto.add(linkTo(methodOn(AvionController.class).actualizarAvion(avion.getID_avion(), null)).withRel("actualizar"));

        return dto;
    }

    public Avion actualizarAvion(Integer id, Avion avionActualizado) {
        Avion avion = avionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avión no encontrado"));

        avion.setMatricula(avionActualizado.getMatricula());
        avion.setMarca(avionActualizado.getMarca());
        avion.setModelo(avionActualizado.getModelo());
        avion.setTipo(avionActualizado.getTipo());

        avion.setCapacidad_pasajero(avionActualizado.getCapacidad_pasajero());
        avion.setCapacidad_carga_kg(avionActualizado.getCapacidad_carga_kg());
        avion.setAlcance_km(avionActualizado.getAlcance_km());
        avion.setCantidad_asientos_vip(avionActualizado.getCantidad_asientos_vip());

        avion.setEnvergadura_metros(avionActualizado.getEnvergadura_metros());
        avion.setCapacidad_combustible(avionActualizado.getCapacidad_combustible());
        avion.setFabricante(avionActualizado.getFabricante());
        avion.setOrigen(avionActualizado.getOrigen());

        normalizarTipo(avion);

        validarTipo(avion);

        return avionRepository.save(avion);
    }

    public List<AvionDTO> buscarPorMatricula(String matricula){
        return avionRepository.buscarPorMatricula(matricula).stream()
                .map(this::convertirADTO)
                .toList();
    }

    private void normalizarTipo(Avion avion) {
        if (avion.getTipo() == null) {
            return;
        }
        try {
            TipoAvion tipoNormalizado = TipoAvion.valueOf(
                    avion.getTipo().getTipo().name().toUpperCase()
            );
            avion.getTipo().setTipo(tipoNormalizado);
        } catch (Exception e) {
            throw new RuntimeException("Tipo de avión inválido (PASAJERO, GUERRA, CARGA, PRIVADO)");
        }
    }

    public Avion patchAvion(Integer id, Avion avion) {
        Avion avion2 = avionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Avion no encontrado"));
        if (avion.getMatricula() != null)
            avion2.setMatricula(avion.getMatricula());
        if (avion.getMarca() != null)
            avion2.setMarca(avion.getMarca());
        if (avion.getModelo() != null)
            avion2.setModelo(avion.getModelo());
        if (avion.getTipo() != null)
            avion2.setTipo(avion.getTipo());
        if (avion.getCapacidad_pasajero() != null)
            avion2.setCapacidad_pasajero(avion.getCapacidad_pasajero());
        if (avion.getCapacidad_carga_kg() != null)
            avion2.setCapacidad_carga_kg(avion.getCapacidad_carga_kg());
        if (avion.getAlcance_km() != null)
            avion2.setAlcance_km(avion.getAlcance_km());
        if (avion.getCantidad_asientos_vip() != null)
            avion2.setCantidad_asientos_vip(avion.getCantidad_asientos_vip());
        if (avion.getEnvergadura_metros() != null)
            avion2.setEnvergadura_metros(avion.getEnvergadura_metros());
        if (avion.getCapacidad_combustible() != null)
            avion2.setCapacidad_combustible(avion.getCapacidad_combustible());
        if (avion.getFabricante() != null)
            avion2.setFabricante(avion.getFabricante());
        if (avion.getOrigen() != null)
            avion2.setOrigen(avion.getOrigen());
        return avionRepository.save(avion2);
    }
}


