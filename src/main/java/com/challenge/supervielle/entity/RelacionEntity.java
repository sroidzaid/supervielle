package com.challenge.supervielle.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "relacion")
public class RelacionEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_relacion")
    private String tipoRelacion;

    @Column(name = "persona_id")
    private PersonaEntity persona;

    @Column(name = "pe_id")
    private Long pe_id;


}
