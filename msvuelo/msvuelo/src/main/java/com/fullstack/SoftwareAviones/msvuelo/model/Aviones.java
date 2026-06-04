package com.fullstack.SoftwareAviones.msvuelo.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "avionesPiloto")
public class Aviones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID_aviones;

    @NotNull(message = "El ID del piloto es obligatorio")
    @Column(name = "ID_piloto", nullable = false)
    private Integer idPiloto;

    @NotNull(message = "El ID del avion es obligatorio")
    @Column(name = "ID_avion", nullable = false)
    private Integer idAvion;

}
