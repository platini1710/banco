package com.bci.tareas.dto;

import java.math.BigDecimal;

public record SobregiroDTO(
        Long idSobregiro,
        Long idCuenta,
        String numeroCuenta,
        BigDecimal montoAprobado,
        BigDecimal montoUtilizado,
        BigDecimal sobregiroDisponible,
        String estado,
        BigDecimal tasaInteres
) {}