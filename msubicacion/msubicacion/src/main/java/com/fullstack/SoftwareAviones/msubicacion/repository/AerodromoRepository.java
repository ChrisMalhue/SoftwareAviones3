package com.fullstack.SoftwareAviones.msubicacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.SoftwareAviones.msubicacion.model.Aerodromo;


@Repository
public interface AerodromoRepository extends JpaRepository<Aerodromo, Integer> {

    

}

