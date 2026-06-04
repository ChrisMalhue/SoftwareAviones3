package com.fullstack.SoftwareAviones.msubicacion.model;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "comunas")
public class Comuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID_comuna;

    @NotBlank (message = "debe espesificarse una comuna obligatoriamente")
    @Size(min = 5, max = 100, message = "la comuna debe tener entre 5 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String comuna;

    @OneToMany(mappedBy = "comuna")
    private List<Aerodromo> aerodromos;

    @ManyToOne
    @JoinColumn(name = "ID_region")
    private Region region;

}
