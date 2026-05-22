package com.bci.tareas.dto;

import java.math.BigDecimal;
// Para actualizar el cupo aprobado (Ej: Aumento de línea de crédito)
public record ActualizarCupoRequest(BigDecimal nuevoMontoAprobado) {}