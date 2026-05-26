package com.bci.tareas.services.impl;
import com.bci.tareas.controllers.ControllerConsulta;
import com.bci.tareas.dto.CuentaConSobregiroDTO;
import com.bci.tareas.exception.RecursoNoEncontradoException;
import com.bci.tareas.model.CuentaBancaria;
import com.bci.tareas.model.Usuario;
import com.bci.tareas.model.enums.EstadoActivo;
import com.bci.tareas.model.enums.EstadoCuentaBancaria;
import com.bci.tareas.model.enums.EstadoSobregiro;
import com.bci.tareas.repositorio.CuentaBancariaRepository;
import com.bci.tareas.repositorio.UsuarioRepository;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CuentaBancariaService {
    private final CuentaBancariaRepository cuentaBancariaRepository;
    private final UsuarioRepository usuarioRepository;
    private static final Logger logger = LoggerFactory.getLogger(CuentaBancariaService.class);

    @Transactional
    public void registrarRetiro(Long idCuenta, BigDecimal montoRetiro) {

        // 1. Buscamos la cuenta y la BLOQUEAMOS en la base de datos
        CuentaBancaria cuenta = cuentaBancariaRepository.findByIdConBloqueo(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        // 2. Validamos reglas de negocio (Nadie puede modificar el saldo mientras hacemos esto)
        if (cuenta.getEstado() != EstadoCuentaBancaria.ACTIVA) {
            throw new IllegalStateException("No se puede operar sobre una cuenta inactiva o bloqueada");
        }

        // Comparamos usando compareTo() que es la forma correcta con BigDecimal
        if (cuenta.getSaldoActual().compareTo(montoRetiro) < 0) {
            throw new IllegalStateException("Fondos insuficientes");
        }

        // 3. Actualizamos el saldo en el objeto Java
        // Usamos .subtract() porque con BigDecimal no se puede usar el signo "-"
        BigDecimal nuevoSaldo = cuenta.getSaldoActual().subtract(montoRetiro);
        cuenta.setSaldoActual(nuevoSaldo);

        // 4. Guardamos (al terminar el método, Spring Boot libera el bloqueo de MySQL automáticamente)
        cuentaBancariaRepository.save(cuenta);
    }
    public List<CuentaConSobregiroDTO> obtenerCuentasPorRut(Long rut) {

        logger.info("obtenerCuentasPorRut rut" + rut);
        Usuario usuario = usuarioRepository.findByRut(rut)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró ningún usuario con el RUT: " + rut));


        Long idUsuario = usuario.getIdUsuario().longValue();

        return cuentaBancariaRepository.findCuentasConSobregiroByUsuario(idUsuario );
    }


  }