package com.challenge.supervielle.controller;

import com.challenge.supervielle.exception.PersonaInexistenteException;
import com.challenge.supervielle.model.PersonaModel;
import com.challenge.supervielle.model.RelacionModel;
import com.challenge.supervielle.model.RelacionRequest;
import com.challenge.supervielle.service.PersonaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "Relaciones")
@Slf4j
public class RelacionesController {

    @Autowired
    private PersonaService personaService;

    @ApiOperation(value = "Obtener Relaciones")
    @PreAuthorize("hasRole('ROLE_supervielle') ")
    @GetMapping("/relaciones/{id1}/{id2}")
    @ResponseBody
    public ResponseEntity<?> getRelaciones(@PathVariable("id1") Long id1, @PathVariable("id2") Long id2) {

        log.info("*****************************************************************************************");
        log.info("Consulta las relaciones entre las personas con ids: " +id1+ " y " +id2);
        log.info("*****************************************************************************************");

        try {
            RelacionModel pm = (RelacionModel) this.personaService.obtenerRelaciones(id1, id2);
            return ResponseEntity.ok(pm);

        }catch (PersonaInexistenteException e){
                log.error("Error al buscar la persona", e);
                return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);

        } catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ApiOperation(value = "Consultar si es padre de")
    @PreAuthorize("hasRole('ROLE_supervielle')")
    @PostMapping("/personas/{id1}/padre/{id2}")
    public ResponseEntity<?> consultarPadre(@PathVariable("id1") Long id1, @PathVariable("id2") Long id2){

        log.info("*****************************************************************************************");
        log.info("Consulta si persona de id " + id1 + " es padre de id: "+ id2);
        log.info("*****************************************************************************************");

        try {
            boolean esPadre = this.personaService.esPadre(id1, id2);

            if(esPadre){
                return ResponseEntity.status(HttpStatus.OK).body(id1+" es padre de "+id2);
            }else{
                return ResponseEntity.status(HttpStatus.OK).body(id1+" NO es padre de "+id2);
            }
        }catch (PersonaInexistenteException e){
            log.error("Error al buscar la persona", e);
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @ApiOperation(value = "Cargar relación")
    @PreAuthorize("hasRole('ROLE_supervielle')")
    @PostMapping("/relacionar")
    public ResponseEntity<?> guardarRelacion(@RequestBody RelacionRequest relacionRequest){

        log.info("*****************************************************************************************");
        log.info("Relacionar personas con id " + relacionRequest.getIdPersona1() + " y id: "+ relacionRequest.getIdPersona2());
        log.info("*****************************************************************************************");

        try {

            this.personaService.guardarRelacion(relacionRequest.getIdPersona1(), relacionRequest.getIdPersona2(), relacionRequest.getRelacion());

            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
