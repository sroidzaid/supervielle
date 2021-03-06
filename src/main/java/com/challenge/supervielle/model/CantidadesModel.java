package com.challenge.supervielle.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CantidadesModel {

    private Long cantidad_hombres;
    private Long cantidad_mujeres;
    private Long cantidad_argentinos;
    private Long cantidad_total;
}
