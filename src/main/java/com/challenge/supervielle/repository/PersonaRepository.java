package com.challenge.supervielle.repository;

import com.challenge.supervielle.entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Map;


@Repository
public interface PersonaRepository extends JpaRepository<PersonaEntity, Long> {

    @Query(value ="SELECT * FROM persona WHERE tipo_documento = ?1 and nro_documento = ?2 and pais = ?3 and sexo = ?4", nativeQuery = true)
    PersonaEntity findPersona(String tipoDocumento, String nroDocumento, String pais, String sexo);

    @Query(value = "SELECT COUNT(CASE WHEN sexo = 'M' THEN 1 END) AS cantidad_hombres, COUNT(CASE WHEN sexo = 'F' THEN 1 END) AS cantidad_mujeres, COUNT(CASE WHEN pais='Argentina' THEN 1 END) AS cantidad_argentinos, COUNT(*) AS cantidad_total FROM persona" , nativeQuery = true)
    Map<String, BigInteger> getEstadisticas();


}
