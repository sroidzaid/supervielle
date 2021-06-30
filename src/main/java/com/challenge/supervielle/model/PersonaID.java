package com.challenge.supervielle.model;


import java.io.Serializable;

public class PersonaID implements Serializable {

    private String tipoDocumento;
    private String nroDocumento;
    private String pais;
    private String sexo;

    public PersonaID(String tipoDocumento, String nroDocumento, String pais, String sexo) {
        this.tipoDocumento = tipoDocumento;
        this.nroDocumento = nroDocumento;
        this.pais = pais;
        this.sexo = sexo;
    }
}
