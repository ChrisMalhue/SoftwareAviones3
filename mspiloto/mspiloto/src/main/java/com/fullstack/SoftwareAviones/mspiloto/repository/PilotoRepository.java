package com.fullstack.SoftwareAviones.mspiloto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fullstack.SoftwareAviones.mspiloto.model.Piloto;

@Repository
public interface PilotoRepository extends JpaRepository<Piloto, Integer> {

    @Query("SELECT p FROM Piloto p WHERE p.nombre = :nombre")
    List<Piloto> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT p FROM Piloto p WHERE p.horas_vuelo >= :horasMinimas")
    List<Piloto> buscarPilotosConHorasMinimas(@Param("horasMinimas") Integer horasMinimas);

}