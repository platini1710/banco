package com.bci.tareas.controllers;

import com.bci.tareas.dto.ActualizarSaldoRequest;
import com.bci.tareas.dto.SobregiroDTO;
import com.bci.tareas.model.SobreGiro;
import com.bci.tareas.services.SobregiroService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController

@RequiredArgsConstructor
@RequestMapping("/sobregiro/cuenta")
public class SobreGiroController {
    private static final Logger logger = LoggerFactory.getLogger(SobreGiroController.class);
    private final SobregiroService sobregiroService;

    // Usamos GET porque solo estamos consultando datos. El RUT viaja en la URL.
    @GetMapping("/usuario/{idCuenta}")
    public ResponseEntity<SobregiroDTO> getCuentasPorIdCuenta(@PathVariable Long idCuenta) {
        logger.info("idCuenta ::" + idCuenta);
// 1. Obtenemos la entidad única
        SobreGiro sobregiro = sobregiroService.encontrarSobreGiroPorIdCuenta(idCuenta);

        // (Opcional) Aquí deberías validar si sobregiro es null para devolver un 404 Not Found

        // 2. Extraemos la cuenta


        // 3. Creamos el DTO
        SobregiroDTO dto = new SobregiroDTO(
                sobregiro.getIdSobregiro(),
                sobregiro.getCuentaBancaria().getIdCuenta(),

                sobregiro.getCuentaBancaria().getNumeroCuenta(),
                sobregiro.getMontoAprobado(),
                sobregiro.getMontoUtilizado(),
                sobregiro.getDisponible(),
                sobregiro.getEstado().name(),
                sobregiro.getTasaInteres()
        );
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/actualizaSaldo/{idCuenta}")
    public ResponseEntity<SobregiroDTO> actualizaSaldo(
            @PathVariable Long idCuenta,
            @RequestBody ActualizarSaldoRequest request) {
        logger.info("actualizaSaldo 1::" + request.nuevoMontoUtilizado());
        // 1. Llamamos a tu servicio pasándole los parámetros

        SobreGiro sobregiroActualizado = sobregiroService.actualizaSaldo(idCuenta, request.nuevoMontoUtilizado());
// 3. Mapeas los datos de la Entidad hacia el DTO
        SobregiroDTO respuestaDTO = new SobregiroDTO(
                sobregiroActualizado.getIdSobregiro().longValue(), // Convertimos a Long si tu ID en la BD era Integer
                sobregiroActualizado.getCuentaBancaria().getIdCuenta(),
                sobregiroActualizado.getCuentaBancaria().getNumeroCuenta(),
                sobregiroActualizado.getMontoAprobado(),
                sobregiroActualizado.getMontoUtilizado(),
                sobregiroActualizado.getDisponible(),
                sobregiroActualizado.getEstado().name(),
                sobregiroActualizado.getTasaInteres()// Convertimos el Enum a String
        );

        // 4. Retornas el DTO limpio al frontend con un 200 OK
     //   return ResponseEntity.ok(respuestaDTO);
        //logger.info("actualizaSaldo  2::" + sobregiroActualizado.getEstado().name());
        // 2. Retornamos el objeto actualizado envuelto en un ResponseEntity (código 200 OK)
        return ResponseEntity.ok(respuestaDTO);
    }
}