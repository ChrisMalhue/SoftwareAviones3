package com.fullstack.SoftwareAviones.msvuelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.SoftwareAviones.msvuelo.model.Vuelo;;

@Repository
public interface VueloRepository extends JpaRepository<Vuelo, Integer> {

}
