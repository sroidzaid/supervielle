package com.challenge.supervielle.controller;

import com.challenge.supervielle.model.EstadisticasModel;
import com.challenge.supervielle.service.PersonaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("estadisticas")
@Api(value = "Estadisticas")
@Slf4j
public class EstadisticasController {

    @Autowired
    private PersonaService personaService;

    @ApiOperation(value = "Obtener Relaciones")
    @PreAuthorize("hasRole('ROLE_SUPERVIELLE') ")
    @GetMapping()
    @ResponseBody
    public ResponseEntity<?> getRelaciones(@PathVariable("id1") Long id1, @PathVariable("id2") Long id2) {

        log.info("*****************************************************************************************");
        log.info("Consulta las relaciones entre las personas con ids: " +id1+ " y " +id2);
        log.info("*****************************************************************************************");

        try {
            EstadisticasModel e = (EstadisticasModel) this.personaService.obtenerEstadisticas();
            return ResponseEntity.ok(e);

        } catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
