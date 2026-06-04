package com.fullstack.SoftwareAviones.mspiloto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.SoftwareAviones.mspiloto.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {

}
