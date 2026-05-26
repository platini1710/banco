package com.bci.tareas.controllers;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.bci.tareas.dto.CuentaBancariaDTO;
import com.bci.tareas.dto.CuentaConSobregiroDTO;
import com.bci.tareas.dto.Login;
import com.bci.tareas.model.CuentaBancaria;
import com.bci.tareas.repositorio.CuentaBancariaRepository;
import com.bci.tareas.security.JwtService;
import com.bci.tareas.services.impl.CuentaBancariaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.services.ConsultaUsuarioService;
import com.bci.tareas.services.RegistraUsuarioServices;





@RestController
@RequestMapping("/consulta/usuario")
@RequiredArgsConstructor
public class ControllerConsulta {

    // 1. Declarar como private final

    private final ConsultaUsuarioService consultaUsuarioService;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(ControllerConsulta.class);
    private final CuentaBancariaService cuentaBancariaService;




    /**
     * Gets users by id.
     *
     * @param userId the user id
     * @return the users by id
     * @throws ResourceNotFoundException the resource not found exception
     */

// Usamos GET porque solo estamos consultando datos. El RUT viaja en la URL.
    @GetMapping("/usuario/{rut}")
    public ResponseEntity<List<CuentaConSobregiroDTO>> getCuentasPorRut(@PathVariable Long rut) {

        logger.info("Obteniendo las cuentas bancarias para el RUT: {}", rut);

        // 1. Obtenemos las entidades desde el Service
        List<CuentaConSobregiroDTO> listCuentasBancarias = cuentaBancariaService.obtenerCuentasPorRut(rut);

        // 2. Convertimos la Lista de Entidades a una Lista de DTOs usando Streams
// Mapeo adaptado para un Java Record
        List<CuentaConSobregiroDTO> cuentaBancariaDTO = listCuentasBancarias.stream()
                .map(cuenta -> new CuentaConSobregiroDTO(
                        cuenta.getIdCuenta(),
                        cuenta.getIdUsuario(),
                        cuenta.getIdBanco(),
                        cuenta.getIdTipoCuenta(),
                        cuenta.getNumeroCuenta(),
                        cuenta.getSaldoActual(),
                        cuenta.getMoneda(),
                        cuenta.getEstado(),
                        cuenta.getFechaApertura(),
                        cuenta.getDisponible(),
                        cuenta.getMontoUtilizado()

                ))
                .toList(); // En Java 16+ esto reemplaza a .collect(Collectors.toList())

        return ResponseEntity.ok(cuentaBancariaDTO);
    }
}