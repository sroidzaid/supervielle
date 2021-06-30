package com.challenge.supervielle.entity;

import com.challenge.supervielle.model.PersonaID;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "persona")
@IdClass(PersonaID.class)
public class PersonaEntity implements Serializable {

    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Id
    private String tipoDocumento;

    @Id
    private String nroDocumento;

    @Id
    private String pais;

    @Id
    private String sexo;

    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @OneToMany(
            mappedBy = "",
            cascade= CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<ContactoEntity> contactos = new ArrayList<>();


}
