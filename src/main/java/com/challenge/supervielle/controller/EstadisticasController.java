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

    @ApiOperation(value = "Obtener Estadisticas")
    @PreAuthorize("hasRole('ROLE_supervielle') ")
    @GetMapping()
    @ResponseBody
    public ResponseEntity<?> getEstadisticas() {

        log.info("*****************************************************************************************");
        log.info("Consulta de estadisticas");
        log.info("*****************************************************************************************");

        try {
            EstadisticasModel e = this.personaService.obtenerEstadisticas();
            return ResponseEntity.ok(e);

        } catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
