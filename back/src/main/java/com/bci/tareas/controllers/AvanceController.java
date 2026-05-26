package com.bci.tareas.controllers;

import com.bci.tareas.dto.SolicitudAvanceDTO;
import com.bci.tareas.model.AvanceDeuda;
import com.bci.tareas.services.impl.AvanceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sobregiro/avance")
@RequiredArgsConstructor
public class AvanceController {

    private final AvanceService avanceService;
    private static final Logger logger = LoggerFactory.getLogger(AvanceController.class);

    @PostMapping("/solicitar")
    public ResponseEntity<AvanceDeuda> solicitarAvance(@RequestBody SolicitudAvanceDTO solicitud) {

            logger.info("solicitud " +solicitud);
            AvanceDeuda avance = avanceService.procesarSolicitudAvance(solicitud);

            // Idealmente transformas "avance" a un AvanceResponseDTO aquí
            return ResponseEntity.ok(avance);


    }
}