package com.challenge.supervielle.controller;

import com.challenge.supervielle.exception.PersonaExistenteException;
import com.challenge.supervielle.exception.PersonaInexistenteException;
import com.challenge.supervielle.exception.PersonaMenorException;
import com.challenge.supervielle.model.PersonaModel;
import com.challenge.supervielle.security.JwtService;
import com.challenge.supervielle.service.PersonaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(value = "Servicio persona")
@Slf4j
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST,RequestMethod.DELETE})
public class PersonaController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PersonaService personaService;

    @ApiOperation(value = "Generar token")
    @GetMapping("/gettoken")
    public String getToken(@AuthenticationPrincipal User usuario) {

        log.info("*****************************************************************************************");
        log.info("Se generó el token de acceso correctamente");
        log.info("*****************************************************************************************");

        List<String> rolesList = usuario
                .getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        return jwtService.createToken(usuario.getUsername(), rolesList);

    }

    @ApiOperation(value = "Buscar persona")
    @PreAuthorize("hasRole('ROLE_supervielle') ")
    @GetMapping("/persona")
    @ResponseBody
    public ResponseEntity<?> getPersona(@RequestParam String tipodoc, @RequestParam String nrodoc, @RequestParam String pais, @RequestParam String sexo) {

        log.info("*****************************************************************************************");
        log.info("Consulta de persona con documento nro: " + nrodoc);
        log.info("*****************************************************************************************");

        try {
            PersonaModel pm = this.personaService.buscarPersona(tipodoc, nrodoc, pais, sexo);
            return ResponseEntity.ok(pm);

        }catch (PersonaInexistenteException e){
            log.error("La persona que intenta buscar no existe ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }  catch (Exception e){
            log.error("Error Inesperado ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @ApiOperation(value = "Guardar persona")
    @PreAuthorize("hasRole('ROLE_supervielle')")
    @PostMapping("/persona")
    public ResponseEntity<?> guardarPersona(@RequestBody @Valid PersonaModel persona){

        log.info("*****************************************************************************************");
        log.info("Nuevo registro de persona con nro documento: " + persona.getNroDocumento());
        log.info("*****************************************************************************************");

        try {
           this.personaService.crearPersona(persona);

           return new ResponseEntity<PersonaModel>(
                   persona,
                    HttpStatus.CREATED);

        }catch (PersonaExistenteException e){
            log.error("La persona que intenta registar ya existe", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }catch (PersonaMenorException e){
            log.error("La persona es menor de edad y no se guardará ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error("Error Inesperado ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @ApiOperation(value = "Actualizacion de los datos de la persona")
    @PreAuthorize("hasRole('ROLE_supervielle')")
    @PutMapping (value = "/updatePersona")
    public ResponseEntity<?> updatePersona(@RequestBody @Valid PersonaModel personaModel){

        log.info("*****************************************************************************************");
        log.info("Actualizacion de datos de persona con nro documento: " + personaModel.getNroDocumento());
        log.info("*****************************************************************************************");

        try {
            this.personaService.updatePersona(personaModel);

            return new ResponseEntity<>(HttpStatus.OK);

        }catch (PersonaInexistenteException e){
            log.error("La persona que intenta modificar no existe ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }  catch (Exception e){
            log.error("Error Inesperado ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @ApiOperation(value = "Borrar Persona")
    @PreAuthorize("hasRole('ROLE_supervielle')")
    @DeleteMapping("/persona/{tipodoc}/{nrodoc}/{pais}/{sexo}")
    public ResponseEntity<?> deletePersona(@PathVariable("tipodoc") String tipodoc, @PathVariable("nrodoc") String nrodoc, @PathVariable("pais") String pais, @PathVariable("sexo") String sexo){

        try {
            this.personaService.deletePersona(tipodoc, nrodoc, pais, sexo);

            return new ResponseEntity<>(HttpStatus.OK);
        }catch (PersonaInexistenteException e){
            log.error("La persona que intenta modificar no existe ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }catch (Exception e){
            log.error("Error Inesperado ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
