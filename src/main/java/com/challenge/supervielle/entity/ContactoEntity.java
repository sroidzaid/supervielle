package com.challenge.supervielle.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "contacto")
public class ContactoEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_contacto")
    private String tipoContacto;

    @Column(name = "descripcion")
    private String descripcion;

    public ContactoEntity(String tipoContacto, String descripcion) {
        this.tipoContacto = tipoContacto;
        this.descripcion = descripcion;
    }

}
