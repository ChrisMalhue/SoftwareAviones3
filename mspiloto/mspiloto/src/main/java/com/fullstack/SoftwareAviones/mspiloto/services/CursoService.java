package com.fullstack.SoftwareAviones.mspiloto.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.SoftwareAviones.mspiloto.DTO.CursoDTO;
import com.fullstack.SoftwareAviones.mspiloto.model.Curso;
import com.fullstack.SoftwareAviones.mspiloto.model.Cursos;
import com.fullstack.SoftwareAviones.mspiloto.repository.CursoRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<CursoDTO> obtenerTodos() {
        log.info("Obteniendo Todos los Datos De Cursos");
        return cursoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public CursoDTO buscarPorId(Integer id) {
        log.info("Buscando Curso Por ID: {}", id);
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡Curso no encontrado!"));

        return convertirADTO(curso);
    }

    public String eliminar(Integer id) {
        log.info("Eliminando Un Curso Del Sistema");
        try {

            Curso curso = cursoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException(
                            "¡Imposible eliminar! El curso con ID " + id + " no existe."
                    ));

            cursoRepository.delete(curso);

            return "El curso '" + curso.getNombre_curso() + "' fue eliminado correctamente.";

        } catch (RuntimeException e) {

            return e.getMessage();
        }
    }

    public CursoDTO guardarCurso(Curso curso) {
        log.info("Registrando Un Nuevo Curso");
        return convertirADTO(cursoRepository.save(curso));
    }

    public CursoDTO actualizarCurso(Integer id, Curso cursoActualizado) {
        log.info("Actualizando Informacion Del Curso con ID: {}", id);
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        curso.setNombre_curso(cursoActualizado.getNombre_curso());
        return convertirADTO(cursoRepository.save(curso));
    }

    private CursoDTO convertirADTO(Curso curso) {
        CursoDTO dto = new CursoDTO();

        dto.setID_curso(curso.getID_curso());
        dto.setNombre_curso(curso.getNombre_curso());

        List<String> pilotos = new ArrayList<>();

        if (curso.getPilotosCurso() != null) {

            for (Cursos cursoPiloto : curso.getPilotosCurso()) {

                pilotos.add(
                        cursoPiloto.getPiloto().getNombre()
                );
            }
        }

        dto.setPilotos(pilotos);

        return dto;
    }

    public CursoDTO patchCurso(Integer id, Curso curso) {
        Curso curso2 = cursoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        if (curso.getNombre_curso() != null) {
            curso2.setNombre_curso(curso.getNombre_curso());
        }
        return convertirADTO(cursoRepository.save(curso2));
    }

}
