package com.challenge.supervielle.service.serviceImpl;

import com.challenge.supervielle.entity.ContactoEntity;
import com.challenge.supervielle.entity.PersonaEntity;
import com.challenge.supervielle.exception.PersonaInexistenteException;
import com.challenge.supervielle.model.*;
import com.challenge.supervielle.repository.PersonaRepository;
import com.challenge.supervielle.service.PersonaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PersonaServiceImpl implements PersonaService<Optional<Object>> {

    @Autowired
    private PersonaRepository personaRepository;


    @Override
    public Optional<Object> buscarPersona(String tipoDocumento, String nroDocumento, String pais, String sexo) {

        PersonaID personaId = new PersonaID(tipoDocumento, nroDocumento, pais, sexo);
        PersonaEntity pe = this.personaRepository.findById(personaId).get();

        Optional<Object> personaModel = this.personaRepository
                .findById(personaId)
                .map((e) -> new PersonaModel(
                        pe.getTipoDocumento(),
                        pe.getNroDocumento(),
                        pe.getPais(),
                        pe.getSexo(),
                        pe.getFechaNacimiento(),
                        pe.getNombre(),
                        pe.getApellido()));

            return personaModel;

    }

    @Override
    public void crearPersona(PersonaModel model){

        try{
            PersonaEntity p = new PersonaEntity();
            p.setTipoDocumento(model.getTipoDocumento());
            p.setNroDocumento(model.getNroDocumento());
            p.setSexo(model.getSexo());
            p.setPais(model.getPais());
            p.setFechaNacimiento(model.getFechaNacimiento());
            p.setNombre(model.getNombre());
            p.setApellido(model.getApellido());

            this.personaRepository.save(p);
        }catch(Exception e){
            log.info("Error al guardar persona");
            throw e;
        }
    }

    @Override
    public void updatePersona(PersonaModel personaModel) throws PersonaInexistenteException {
        PersonaID personaId = new PersonaID(personaModel.getTipoDocumento(), personaModel.getNroDocumento(), personaModel.getPais(), personaModel.getSexo());
        PersonaEntity pe = this.personaRepository.findById(personaId).get();
        if(pe != null){
            pe.setNombre(personaModel.getNombre());
            pe.setApellido(personaModel.getApellido());
            pe.setFechaNacimiento(personaModel.getFechaNacimiento());

            List<ContactoEntity> nuevosContactos = new ArrayList();
            for(ContactoModel contacto: personaModel.getContactos()){
                ContactoEntity contactoEntity = new ContactoEntity(contacto.getTipoContacto(), contacto.getDescripcion());
                nuevosContactos.add(contactoEntity);
            }
            if(pe.getContactos()!=null){
                pe.getContactos().addAll(nuevosContactos);
            }else{
                pe.setContactos(nuevosContactos);
            }
            this.personaRepository.save(pe);
        }else {
            throw new PersonaInexistenteException("No existe la persona que intenta modificar");
        }
    }

    @Override
    public void deletePersona(String tipoDocumento, String nroDocumento, String pais, String sexo) throws PersonaInexistenteException {

        PersonaID personaId = new PersonaID(tipoDocumento, nroDocumento, pais, sexo);
        PersonaEntity pe = this.personaRepository.findById(personaId).get();

        if (pe != null){
            this.personaRepository.delete(pe);
        }else{
            throw new PersonaInexistenteException("No existe la persona que intenta eliminar");
        }
    }

    @Override
    public boolean esPadre(Long id1, Long id2) {
        return false;
    }

    @Override
    public EstadisticasModel obtenerEstadisticas() {
        return null;
    }

    @Override
    public RelacionModel obtenerRelaciones(Long id1, Long id2) {
        return null;
    }

    @Override
    public void guardarRelacion(Long id1, Long id2) {

    }
}
