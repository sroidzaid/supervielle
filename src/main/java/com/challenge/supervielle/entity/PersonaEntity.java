package com.challenge.supervielle.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "persona")
public class PersonaEntity implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "nro_documento")
    private String nroDocumento;

    @Column(name = "pais")
    private String pais;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pe_id")
    private List<ContactoEntity> contactos;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pe_id")
    private List<RelacionEntity> relaciones;


}
