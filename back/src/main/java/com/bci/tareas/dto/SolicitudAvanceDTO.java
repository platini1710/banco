package com.bci.tareas.dto;

import java.math.BigDecimal;

public record SolicitudAvanceDTO(
        Long idCuenta,
        BigDecimal montoSolicitado,
        Integer cantidadCuotas
) {}