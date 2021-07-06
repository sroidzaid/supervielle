package com.challenge.supervielle.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelacionRequest {

    private Long idPersona1;
    private Long idPersona2;
    private String relacion;


}
