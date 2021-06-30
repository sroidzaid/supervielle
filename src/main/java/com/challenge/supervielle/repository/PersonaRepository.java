package com.challenge.supervielle.repository;

import com.challenge.supervielle.entity.PersonaEntity;
import com.challenge.supervielle.model.EstadisticasModel;
import com.challenge.supervielle.model.PersonaID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface PersonaRepository extends JpaRepository<PersonaEntity, PersonaID> {

    @Query("SELECT * FROM PersonaEntity pe WHERE pe.id = ?1")
    PersonaEntity findPersonaById(Long id);

    @Query(value = "SELECT COUNT(CASE WHEN sexo = 'M' THEN 1 END) AS cantidad_hombres, COUNT(CASE WHEN sexo = 'F' THEN 1 END) AS cantidad_mujeres, COUNT(CASE WHEN pais='Argentina' THEN 1 END) AS cantidad_argentinos, COUNT(*) AS cantidad_total FROM persona)" , nativeQuery = true)
    EstadisticasModel getEstadisticas();

}
