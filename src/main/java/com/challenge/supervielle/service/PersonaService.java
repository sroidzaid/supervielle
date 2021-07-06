package com.challenge.supervielle.service;

import com.challenge.supervielle.exception.PersonaExistenteException;
import com.challenge.supervielle.exception.PersonaInexistenteException;
import com.challenge.supervielle.exception.PersonaMenorException;
import com.challenge.supervielle.model.EstadisticasModel;
import com.challenge.supervielle.model.PersonaModel;
import com.challenge.supervielle.model.RelacionModel;

import java.text.ParseException;

public interface PersonaService<T> {

    PersonaModel buscarPersona(String tipoDocumento, String nroDocumento, String pais, String sexo) throws PersonaInexistenteException;
    void crearPersona(PersonaModel personaModel) throws ParseException, PersonaMenorException, PersonaExistenteException;
    void updatePersona(PersonaModel personaModel) throws PersonaInexistenteException, ParseException;
    void deletePersona(String tipoDocumento, String nroDocumento, String pais, String sexo) throws PersonaInexistenteException;

    boolean esPadre(Long id1, Long id2) throws PersonaInexistenteException;
    EstadisticasModel obtenerEstadisticas();
    RelacionModel obtenerRelaciones(Long id1, Long id2) throws PersonaInexistenteException;
    void guardarRelacion(Long id1, Long id2, String relacion);
}
