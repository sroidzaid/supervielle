package com.challenge.supervielle.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PersonaModel {

    @NotEmpty(message="El tipo de documento es requerido")
    @NotNull
    private String tipoDocumento;
    @NotEmpty(message="El nro de documento es requerido")
    @NotNull
    private String nroDocumento;
    @NotEmpty(message="El pais puede es requerido")
    @NotNull
    private String pais;
    @NotEmpty(message="El sexo puede es requerido")
    @NotNull
    private String sexo;
    @NotEmpty(message="La fecha de nacimiento es requerida")
    @NotNull
    private Date fechaNacimiento;
    @Min(18)
    private int edad;
    private String nombre;
    private String apellido;
    @NotEmpty(message="La persona debe tener al menos un contacto")
    @NotNull
    private List<ContactoModel> contactos = new ArrayList<>();



    public PersonaModel(String tipoDocumento, String nroDocumento, String pais, String sexo, Date fechaNacimiento, String nombre, String apellido) {
        this.tipoDocumento = tipoDocumento;
        this.nroDocumento = nroDocumento;
        this.pais = pais;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.nombre = nombre;
        this.apellido = apellido;
        getEdad();
    }

    public PersonaModel(String tipoDocumento, String nroDocumento, String pais, String sexo, Date fechaNacimiento) {
        this.tipoDocumento = tipoDocumento;
        this.nroDocumento = nroDocumento;
        this.pais = pais;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        getEdad();
    }

    private void getEdad(){
        if(this.fechaNacimiento!=null){
            Period periodEdad = Period.between(fechaNacimiento.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate(), LocalDate.now());
            this.edad = periodEdad.getYears();
        }
    }

}
