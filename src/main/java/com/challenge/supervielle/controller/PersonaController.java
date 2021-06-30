package com.challenge.supervielle.controller;

import com.challenge.supervielle.exception.PersonaInexistenteException;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(value = "Servicio persona")
@Slf4j
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
    @PreAuthorize("hasRole('ROLE_SUPERVIELLE') ")
    @GetMapping("/persona")
    @ResponseBody
    public ResponseEntity<?> getPersona(@RequestParam String tipodoc, @RequestParam String nrodoc, @RequestParam String pais, @RequestParam String sexo) {

        log.info("*****************************************************************************************");
        log.info("Consulta de persona con documento nro: " + nrodoc);
        log.info("*****************************************************************************************");

        try {
            PersonaModel pm = (PersonaModel) this.personaService.buscarPersona(tipodoc, nrodoc, pais, sexo);
            return ResponseEntity.ok(pm);

        } catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ApiOperation(value = "Guardar persona")
    @PreAuthorize("hasRole('ROLE_SUPERVIELLE')")
    @PostMapping("/persona")
    public ResponseEntity<?> guardarPersona(@RequestBody @Validated PersonaModel persona){

        log.info("*****************************************************************************************");
        log.info("Nuevo registro de persona con nro documento: " + persona.getNroDocumento());
        log.info("*****************************************************************************************");

        try {
           this.personaService.crearPersona(persona);

           return new ResponseEntity<PersonaModel>(
                   persona,
                    HttpStatus.CREATED);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @ApiOperation(value = "Actualizacion de los datos de la persona")
    @PreAuthorize("hasRole('ROLE_SUPERVIELLE')")
    @PutMapping (value = "/updatePersona")
    public ResponseEntity<?> updatePersona(@RequestBody PersonaModel personaModel){

        log.info("*****************************************************************************************");
        log.info("Actualizacion de datos de persona con nro documento: " + personaModel.getNroDocumento());
        log.info("*****************************************************************************************");

        try {
            this.personaService.updatePersona(personaModel);

            return new ResponseEntity<>(HttpStatus.OK);

        }catch (PersonaInexistenteException e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }  catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @ApiOperation(value = "Borrar Persona")
    @PreAuthorize("hasRole('ROLE_SUPERVIELLE')")
    @DeleteMapping("/{tipodoc}/{nrodoc}/{pais}/{sexo}/")
    public ResponseEntity<?> deletePersona(@PathVariable("tipodoc") String tipodoc, @PathVariable("nrodoc") String nrodoc, @PathVariable("pais") String pais, @PathVariable("sexo") String sexo){

        try {
            this.personaService.deletePersona(tipodoc, nrodoc, pais, sexo);

            return new ResponseEntity<>(HttpStatus.OK);
        }catch (PersonaInexistenteException e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);

    }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
