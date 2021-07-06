package com.challenge.supervielle.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadisticasModel {

    private Long cantidad_hombres;
    private Long cantidad_mujeres;
    private Double porcentaje_argentinos;

    public EstadisticasModel(Long cantidad_hombres, Long cantidad_mujeres, Double porcentaje_argentinos) {
        this.cantidad_hombres = cantidad_hombres;
        this.cantidad_mujeres = cantidad_mujeres;
        this.porcentaje_argentinos = porcentaje_argentinos;
    }

}
