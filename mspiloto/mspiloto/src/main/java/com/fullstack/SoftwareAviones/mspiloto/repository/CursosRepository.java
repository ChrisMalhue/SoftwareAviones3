package com.fullstack.SoftwareAviones.mspiloto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.SoftwareAviones.mspiloto.model.Cursos;

@Repository
public interface CursosRepository extends JpaRepository<Cursos, Integer> {

}