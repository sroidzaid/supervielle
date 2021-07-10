package com.challenge.supervielle.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaModel {

    @NotBlank(message="El tipo de documento es requerido")
    @NotNull
    private String tipoDocumento;
    @NotBlank(message="El nro de documento es requerido")
    @NotNull
    private String nroDocumento;
    @NotBlank(message="El pais es requerido")
    @NotNull
    private String pais;
    @NotBlank(message="El sexo es requerido")
    @NotNull
    private String sexo;
    @NotBlank(message="La fecha de nacimiento es requerida")
    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String fechaNacimiento;
    private String nombre;
    private String apellido;
    @NotEmpty(message="La persona debe tener al menos un contacto")
    @NotNull
    private List<ContactoModel> contactos = new ArrayList<>();



    public PersonaModel(String tipoDocumento, String nroDocumento, String pais, String sexo, String fechaNacimiento, String nombre, String apellido) {
        this.tipoDocumento = tipoDocumento;
        this.nroDocumento = nroDocumento;
        this.pais = pais;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.nombre = nombre;
        this.apellido = apellido;
        //getEdad();
    }

    public PersonaModel(String tipoDocumento, String nroDocumento, String pais, String sexo, String fechaNacimiento) {
        this.tipoDocumento = tipoDocumento;
        this.nroDocumento = nroDocumento;
        this.pais = pais;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad(){
        if(this.fechaNacimiento!=null && !this.fechaNacimiento.isEmpty()){
            Date fechaNacDate= null;
            try {
                fechaNacDate = new SimpleDateFormat("dd-MM-yyyy").parse(this.fechaNacimiento);
                Period periodEdad = Period.between(fechaNacDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate(), LocalDate.now());
                return periodEdad.getYears();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return 0;
    }

}
