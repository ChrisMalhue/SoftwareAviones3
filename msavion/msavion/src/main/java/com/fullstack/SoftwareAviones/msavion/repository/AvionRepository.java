package com.fullstack.SoftwareAviones.msavion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fullstack.SoftwareAviones.msavion.model.Avion;

@Repository
public interface AvionRepository extends JpaRepository<Avion, Integer> {
    @Query("SELECT a FROM Avion a WHERE a.matricula = :matricula")
    List<Avion> buscarPorMatricula(@Param("matricula") String matricula);
    
}
