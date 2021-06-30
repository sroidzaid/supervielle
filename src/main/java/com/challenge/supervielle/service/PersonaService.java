package com.challenge.supervielle.service;

import com.challenge.supervielle.exception.PersonaInexistenteException;
import com.challenge.supervielle.model.EstadisticasModel;
import com.challenge.supervielle.model.PersonaModel;
import com.challenge.supervielle.model.RelacionModel;

public interface PersonaService<T> {

    T buscarPersona(String tipoDocumento, String nroDocumento, String pais, String sexo);
    void crearPersona(PersonaModel personaModel);
    void updatePersona(PersonaModel personaModel) throws PersonaInexistenteException;
    void deletePersona(String tipoDocumento, String nroDocumento, String pais, String sexo) throws PersonaInexistenteException;

    boolean esPadre(Long id1, Long id2);
    EstadisticasModel obtenerEstadisticas();
    RelacionModel obtenerRelaciones(Long id1, Long id2);
    void guardarRelacion(Long id1, Long id2);
}
