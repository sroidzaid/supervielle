package com.challenge.supervielle.service.serviceImpl;

import com.challenge.supervielle.entity.ContactoEntity;
import com.challenge.supervielle.entity.PersonaEntity;
import com.challenge.supervielle.entity.RelacionEntity;
import com.challenge.supervielle.exception.PersonaExistenteException;
import com.challenge.supervielle.exception.PersonaInexistenteException;
import com.challenge.supervielle.exception.PersonaMenorException;
import com.challenge.supervielle.model.ContactoModel;
import com.challenge.supervielle.model.EstadisticasModel;
import com.challenge.supervielle.model.PersonaModel;
import com.challenge.supervielle.model.RelacionModel;
import com.challenge.supervielle.repository.PersonaRepository;
import com.challenge.supervielle.service.PersonaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PersonaServiceImpl implements PersonaService<Optional<Object>> {

    @Autowired
    private PersonaRepository personaRepository;


    @Override
    public PersonaModel buscarPersona(String tipoDocumento, String nroDocumento, String pais, String sexo) throws PersonaInexistenteException {

        PersonaEntity pe = this.personaRepository.findPersona(tipoDocumento, nroDocumento, pais, sexo);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        if (pe!=null ){

            ArrayList<ContactoModel> listContactos = new ArrayList<>();
            for(ContactoEntity contactoEntity: pe.getContactos()){
                ContactoModel contactoModel = new ContactoModel(contactoEntity.getTipoContacto(), contactoEntity.getDescripcion());
                listContactos.add(contactoModel);
            }

            return new PersonaModel(
                    pe.getTipoDocumento(),
                    pe.getNroDocumento(),
                    pe.getPais(),
                    pe.getSexo(),
                    format.format(pe.getFechaNacimiento()),
                    pe.getNombre(),
                    pe.getApellido(),
                    listContactos);
        }else{
                throw new PersonaInexistenteException("No existe la persona que esta buscando");
        }


    }

    @Override
    public void crearPersona(PersonaModel model) throws ParseException, PersonaMenorException, PersonaExistenteException {

        try{
            if(model.getEdad()>=18){
                PersonaEntity pe = this.personaRepository.findPersona(model.getTipoDocumento(), model.getNroDocumento(), model.getPais(), model.getSexo());
                if(pe!=null){
                    throw new PersonaExistenteException("La persona ya existe");
                }else{
                    PersonaEntity p = new PersonaEntity();
                    p.setTipoDocumento(model.getTipoDocumento());
                    p.setNroDocumento(model.getNroDocumento());
                    p.setSexo(model.getSexo());
                    p.setPais(model.getPais());
                    p.setFechaNacimiento(new SimpleDateFormat("dd-MM-yyyy").parse(model.getFechaNacimiento()));
                    p.setNombre(model.getNombre());
                    p.setApellido(model.getApellido());
                    List<ContactoEntity> listaContactos = new ArrayList();
                    for(ContactoModel contactoModel: model.getContactos()){
                        ContactoEntity contactoEntity = new ContactoEntity(contactoModel.getTipoContacto(), contactoModel.getDescripcion());
                        listaContactos.add(contactoEntity);
                    }
                    p.setContactos(listaContactos);

                    this.personaRepository.save(p);
                }

            }else{
                throw new PersonaMenorException("La persona es menor de edad y no se guardará");
            }
        }catch(ParseException e){
            log.info("Error al parsear la fecha");
            throw e;
        }catch(Exception e){
            log.info("Error al guardar persona");
            throw e;
        }
    }

    @Override
    public void updatePersona(PersonaModel personaModel) throws PersonaInexistenteException, ParseException {
        PersonaEntity pe = this.personaRepository.findPersona(personaModel.getTipoDocumento(), personaModel.getNroDocumento(), personaModel.getPais(), personaModel.getSexo());
        if(pe != null){
            pe.setNombre(personaModel.getNombre());
            pe.setApellido(personaModel.getApellido());
            pe.setFechaNacimiento(new SimpleDateFormat("dd-MM-yyyy").parse(personaModel.getFechaNacimiento()));

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

        PersonaEntity pe = this.personaRepository.findPersona(tipoDocumento, nroDocumento, pais, sexo);

        if (pe != null){
            this.personaRepository.delete(pe);
        }else{
            throw new PersonaInexistenteException("No existe la persona que intenta eliminar");
        }
    }

    @Override
    public boolean esPadre(Long id1, Long id2) throws PersonaInexistenteException {
        PersonaEntity pe = this.personaRepository.findById(id1).get();
        if(pe!=null){
            RelacionModel relacionModel = new RelacionModel();
            relacionModel.setRelacion("No hay relacion entra las personas");
            for(RelacionEntity personaRelacion : pe.getRelaciones()){
                if(personaRelacion.getPersona().getId().equals(id2) && personaRelacion.getTipoRelacion().equals("Padre")){
                    return true;
                }
            }
            return false;
        }else{
            throw new PersonaInexistenteException("La persona con id: "+id1+" no existe");
        }
    }

    @Override
    public EstadisticasModel obtenerEstadisticas() {
        try {
            Map<String, BigInteger> cant = this.personaRepository.getEstadisticas();
            long cantidad_argentinos = cant.get("cantidad_argentinos").longValue();
            long cantidad_total = cant.get("cantidad_total").longValue();
            double porcentaje = 0;
            if(cantidad_argentinos!=0){
                porcentaje = Math.round(((double)cantidad_argentinos/cantidad_total)*100);
            }
            EstadisticasModel estadisticas = new EstadisticasModel(cant.get("cantidad_hombres").longValue(), cant.get("cantidad_mujeres").longValue(), porcentaje);
            return estadisticas;
        }catch(Exception e){
            log.error("Error al obtener estadisticas", e);
            throw e;
        }

    }

    @Override
    public RelacionModel obtenerRelaciones(Long id1, Long id2) throws PersonaInexistenteException {
        PersonaEntity pe = this.personaRepository.findById(id1).get();
        if(pe!=null){
            RelacionModel relacionModel = new RelacionModel();
            relacionModel.setRelacion("No hay relacion entra las personas");
            for(RelacionEntity personaRelacion : pe.getRelaciones()){
                if(personaRelacion.getPersona().getId().equals(id2)){
                    relacionModel.setRelacion(personaRelacion.getTipoRelacion());
                }
            }
            return relacionModel;
        }else{
            throw new PersonaInexistenteException("La persona con id: "+id1+" no existe");
        }
    }

    @Override
    public void guardarRelacion(Long id1, Long id2, String relacion) {

        PersonaEntity pe1 = this.personaRepository.findById(id1).get();
        PersonaEntity pe2 = this.personaRepository.findById(id2).get();
        RelacionEntity relacionEntity = new RelacionEntity();
        relacionEntity.setTipoRelacion(relacion);
        relacionEntity.setPersona(pe2);

        pe1.getRelaciones().add(relacionEntity);
        personaRepository.save(pe1);

    }
}
