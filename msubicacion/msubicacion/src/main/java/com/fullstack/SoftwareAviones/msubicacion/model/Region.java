package com.fullstack.SoftwareAviones.msubicacion.model;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "regiones")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID_region;

    @NotBlank (message = "debe espesificarse una region obligatoriamente")
    @Size(min = 5, max = 100, message = "la region debe tener entre 5 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String region;

    @OneToMany(mappedBy = "region")
    private List<Comuna> comunas;

}
