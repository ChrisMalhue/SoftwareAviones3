package com.fullstack.SoftwareAviones.mspiloto.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.SoftwareAviones.mspiloto.DTO.CursosDTO;
import com.fullstack.SoftwareAviones.mspiloto.model.Cursos;
import com.fullstack.SoftwareAviones.mspiloto.repository.CursosRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CursosService {

    @Autowired
    private CursosRepository cursosRepository;

    public List<CursosDTO> obtenerTodos() {
        return cursosRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public CursosDTO buscarPorId(Integer id) {
        Cursos cursos = cursosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡Registro no encontrado!"));
        return convertirADTO(cursos);
    }  
    
    public String eliminar(Integer id) {
        try {
            Cursos cursos = cursosRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("¡Imposible eliminar! El registro con ID " + id + " no existe."));
            cursosRepository.delete(cursos);
            return "El registro fue eliminado correctamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
    
    public Cursos guardarCursos(Cursos cursos) {
        return cursosRepository.save(cursos);
    } 
    
    public Cursos actualizarCursos(Integer id, Cursos cursosActualizado) {
        Cursos cursos = cursosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        cursos.setPiloto(cursosActualizado.getPiloto());
        cursos.setCurso(cursosActualizado.getCurso());

        return cursosRepository.save(cursos);
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
