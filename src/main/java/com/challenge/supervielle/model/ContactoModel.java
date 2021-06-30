package com.challenge.supervielle.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
public class ContactoModel {

    private String tipoContacto;
    private String descripcion;
}
