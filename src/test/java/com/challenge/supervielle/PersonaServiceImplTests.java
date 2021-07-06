package com.challenge.supervielle;

import com.challenge.supervielle.entity.ContactoEntity;
import com.challenge.supervielle.entity.PersonaEntity;
import com.challenge.supervielle.entity.RelacionEntity;
import com.challenge.supervielle.exception.PersonaExistenteException;
import com.challenge.supervielle.exception.PersonaInexistenteException;
import com.challenge.supervielle.exception.PersonaMenorException;
import com.challenge.supervielle.model.ContactoModel;
import com.challenge.supervielle.model.PersonaModel;
import com.challenge.supervielle.model.RelacionModel;
import com.challenge.supervielle.repository.PersonaRepository;
import com.challenge.supervielle.service.serviceImpl.PersonaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.Assert.*;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class PersonaServiceImplTests {


	@Spy
	@InjectMocks
	PersonaServiceImpl personaServiceImpl;
	@Mock
	PersonaRepository personaRepository;

	private PersonaModel personaModel;
	private PersonaModel personaModel2;
	private PersonaEntity personaEntity;


	@BeforeEach
	public void setUp() {
		PersonaModel personaModel = new PersonaModel();
		personaModel.setTipoDocumento("DNI");
		personaModel.setNroDocumento("32523092");
		personaModel.setPais("Argentina");
		personaModel.setSexo("M");
		personaModel.setNombre("Sebastian");
		personaModel.setApellido("Roidzaid");
		personaModel.setFechaNacimiento("30/07/1986");
		List<ContactoModel> contactos = new ArrayList();
		ContactoModel contactoModel = new ContactoModel("mail", "sebastian.roidzaid@gmail.com");
		contactos.add(contactoModel);
		personaModel.setContactos(contactos);
		this.personaModel = personaModel;

		PersonaModel personaModel2 = new PersonaModel();
		personaModel2.setTipoDocumento("DNI");
		personaModel2.setNroDocumento("32523092");
		personaModel2.setPais("Argentina");
		personaModel2.setSexo("M");
		personaModel2.setNombre("Juan");
		personaModel2.setApellido("Perez");
		personaModel2.setFechaNacimiento("30/07/1986");
		List<ContactoModel> contactos2 = new ArrayList();
		ContactoModel contactoModel2 = new ContactoModel("mail", "sebastian.roidzaid@gmail.com");
		contactos.add(contactoModel2);
		personaModel2.setContactos(contactos2);
		this.personaModel2 = personaModel2;

		PersonaEntity personaEntity = new PersonaEntity();
		personaEntity.setId(23L);
		personaEntity.setTipoDocumento("DNI");
		personaEntity.setNroDocumento("32523092");
		personaEntity.setPais("Argentina");
		personaEntity.setSexo("M");
		personaEntity.setNombre("Sebastian");
		personaEntity.setApellido("Roidzaid");
		try {
			personaEntity.setFechaNacimiento(new SimpleDateFormat("dd/MM/yyyy").parse("30/07/1986"));
		} catch (ParseException e) {
			personaEntity.setFechaNacimiento(new Date());
		}
		List<ContactoEntity> contactosEntity = new ArrayList();
		ContactoEntity contactoEntity = new ContactoEntity("mail", "sebastian.roidzaid@gmail.com");
		contactosEntity.add(contactoEntity);
		personaEntity.setContactos(contactosEntity);

		this.personaEntity =  personaEntity;


	}

	@Test
	public void crearPersonaTest() throws ParseException, PersonaMenorException, PersonaExistenteException, PersonaInexistenteException {
		this.personaServiceImpl.crearPersona(this.personaModel);
		verify(personaRepository, times(1)).save(any());

	}

	@Test
	public void buscarPersonaTest() throws Exception {
		Mockito.when(personaRepository.findPersona(personaModel.getTipoDocumento(), personaModel.getNroDocumento(), personaModel.getPais(), personaModel.getSexo()))
				.thenReturn(personaEntity);
		this.personaServiceImpl.buscarPersona(personaModel.getTipoDocumento(), personaModel.getNroDocumento(), personaModel.getPais(), personaModel.getSexo());
		verify(personaRepository, times(1)).findPersona(personaModel.getTipoDocumento(), personaModel.getNroDocumento(), personaModel.getPais(), personaModel.getSexo());
	}


	@Test
	public void updatePersonaTest() throws PersonaInexistenteException, ParseException {
		Mockito.when(personaRepository.findPersona(personaModel.getTipoDocumento(), personaModel.getNroDocumento(), personaModel.getPais(), personaModel.getSexo()))
				.thenReturn(personaEntity);
		this.personaServiceImpl.updatePersona(this.personaModel2);
		verify(personaRepository, times(1)).save(any());
	}
	@Test
	public void deletePersonaTest() throws PersonaInexistenteException {
		Mockito.when(personaRepository.findPersona(personaModel2.getTipoDocumento(), personaModel2.getNroDocumento(), personaModel2.getPais(), personaModel2.getSexo()))
				.thenReturn(personaEntity);
		this.personaServiceImpl.deletePersona(this.personaModel2.getTipoDocumento(), this.personaModel2.getNroDocumento(), this.personaModel2.getPais(), this.personaModel2.getSexo());
		verify(personaRepository, times(1)).delete(personaEntity);
	}

	@Test
	public void obtenerEstadisticasTest(){

		Map<String, BigInteger> cantindades = new HashMap<>();
		cantindades.put("cantidad_hombres", new BigInteger("1000"));
		cantindades.put("cantidad_mujeres", new BigInteger("1200"));
		cantindades.put("cantidad_argentinos", new BigInteger("800"));
		cantindades.put("cantidad_total", new BigInteger("2200"));

		Mockito.when(personaRepository.getEstadisticas())
				.thenReturn(cantindades);
		this.personaServiceImpl.obtenerEstadisticas();
		verify(personaRepository, times(1)).getEstadisticas();
	}

	@Test
	public void esPadreTest() throws PersonaInexistenteException {

		Long idPersona1 = 10L;
		Long idPersona2 = 23L;

		PersonaEntity personaEntityPadre = new PersonaEntity();
		personaEntityPadre.setId(10L);
		personaEntityPadre.setTipoDocumento("DNI");
		personaEntityPadre.setNroDocumento("31752067");
		personaEntityPadre.setPais("Argentina");
		personaEntityPadre.setSexo("M");
		personaEntityPadre.setNombre("Roberto");
		personaEntityPadre.setApellido("Garcia");
		try {
			personaEntityPadre.setFechaNacimiento(new SimpleDateFormat("dd/MM/yyyy").parse("02/07/1986"));
		} catch (ParseException e) {
			personaEntityPadre.setFechaNacimiento(new Date());
		}
		List<ContactoEntity> contactosEntity = new ArrayList();
		ContactoEntity contactoEntity = new ContactoEntity("mail", "roberto.garcia@gmail.com");
		contactosEntity.add(contactoEntity);
		personaEntityPadre.setContactos(contactosEntity);

		List<RelacionEntity> relacionEntities = new ArrayList();
		RelacionEntity relacionEntity = new RelacionEntity(3L,"Padre", this.personaEntity, 10L);
		relacionEntities.add(relacionEntity);
		personaEntityPadre.setRelaciones(relacionEntities);

		Mockito.when(personaRepository.findById(idPersona1))
				.thenReturn(Optional.of(personaEntityPadre));

		assertTrue(this.personaServiceImpl.esPadre(idPersona1, idPersona2));

	}

	@Test
	public void obtenerRelacionTest() throws PersonaInexistenteException {
		Long idPersona1 = 10L;
		Long idPersona2 = 23L;

		PersonaEntity personaEntityPadre = new PersonaEntity();
		personaEntityPadre.setId(10L);
		personaEntityPadre.setTipoDocumento("DNI");
		personaEntityPadre.setNroDocumento("31752067");
		personaEntityPadre.setPais("Argentina");
		personaEntityPadre.setSexo("M");
		personaEntityPadre.setNombre("Roberto");
		personaEntityPadre.setApellido("Garcia");
		try {
			personaEntityPadre.setFechaNacimiento(new SimpleDateFormat("dd/MM/yyyy").parse("02/07/1986"));
		} catch (ParseException e) {
			personaEntityPadre.setFechaNacimiento(new Date());
		}
		List<ContactoEntity> contactosEntity = new ArrayList();
		ContactoEntity contactoEntity = new ContactoEntity("mail", "roberto.garcia@gmail.com");
		contactosEntity.add(contactoEntity);
		personaEntityPadre.setContactos(contactosEntity);

		List<RelacionEntity> relacionEntities = new ArrayList();
		RelacionEntity relacionEntity = new RelacionEntity(3L,"TIO", this.personaEntity, 10L);
		relacionEntities.add(relacionEntity);
		personaEntityPadre.setRelaciones(relacionEntities);

		Mockito.when(personaRepository.findById(idPersona1))
				.thenReturn(Optional.of(personaEntityPadre));

		RelacionModel relacionModel = this.personaServiceImpl.obtenerRelaciones(idPersona1, idPersona2);
		assertEquals("TIO", relacionModel.getRelacion());
	}

}
