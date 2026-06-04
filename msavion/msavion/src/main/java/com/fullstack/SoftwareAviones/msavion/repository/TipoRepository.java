package com.fullstack.SoftwareAviones.msavion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.SoftwareAviones.msavion.model.Tipo;

@Repository
public interface TipoRepository extends JpaRepository<Tipo, Integer> {

}

