package com.fullstack.SoftwareAviones.mspiloto.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.SoftwareAviones.mspiloto.DTO.CursosDTO;
import com.fullstack.SoftwareAviones.mspiloto.model.Cursos;
import com.fullstack.SoftwareAviones.mspiloto.repository.CursoRepository;
import com.fullstack.SoftwareAviones.mspiloto.repository.CursosRepository;
import com.fullstack.SoftwareAviones.mspiloto.repository.PilotoRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CursosService {

    @Autowired
    private CursosRepository cursosRepository;

    @Autowired
    private PilotoRepository pilotoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public List<CursosDTO> obtenerTodos() {
        log.info("Obteniendo Todos los Datos De Cursos");
        return cursosRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public CursosDTO buscarPorId(Integer id) {
        log.info("Buscando Cursos Por ID: {}", id);
        Cursos cursos = cursosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡Registro no encontrado!"));
        return convertirADTO(cursos);
    }  
    
    public String eliminar(Integer id) {
        log.info("Eliminando Un Registro De Cursos");
        try {
            Cursos cursos = cursosRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! El registro con ID " + id + " no existe."));
            cursosRepository.delete(cursos);
            return "El registro fue eliminado correctamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
    
    public CursosDTO guardarCursos(Cursos cursos) {
        log.info("Registrando Un Nuevo Registro De Cursos");
        cursos.setPiloto(pilotoRepository.findById(cursos.getPiloto().getID_piloto())
            .orElseThrow(() -> new RuntimeException("Piloto no encontrado")));

        cursos.setCurso(cursoRepository.findById(cursos.getCurso().getID_curso())
            .orElseThrow(() -> new RuntimeException("Curso no encontrado")));

        return convertirADTO(cursosRepository.save(cursos));
    }
    
    public CursosDTO actualizarCursos(Integer id, Cursos cursosActualizado) {
        log.info("Actualizando Informacion Del Registro De Cursos con ID: {}", id);
        Cursos cursos = cursosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        cursos.setPiloto(pilotoRepository.findById(cursosActualizado.getPiloto().getID_piloto())
            .orElseThrow(() -> new RuntimeException("Piloto no encontrado")));

        cursos.setCurso(cursoRepository.findById(cursosActualizado.getCurso().getID_curso())
            .orElseThrow(() -> new RuntimeException("Curso no encontrado")));

        return convertirADTO(cursosRepository.save(cursos));
    }
    
    private CursosDTO convertirADTO(Cursos cursos) {
        CursosDTO dto = new CursosDTO();
        dto.setID_cursos(cursos.getID_cursos());
        if (cursos.getPiloto() != null) {
            dto.setPiloto(
                cursos.getPiloto().getNombre()
            );
        }

        if (cursos.getCurso() != null) {
            dto.setCurso(
                cursos.getCurso().getNombre_curso()
            );
        }
        return dto;
    }    

}
