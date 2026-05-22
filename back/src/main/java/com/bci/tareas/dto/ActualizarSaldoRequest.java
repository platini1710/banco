package com.bci.tareas.dto;

import java.math.BigDecimal;

// Puedes poner esto al final de tu archivo Controller o en un archivo separado
public record ActualizarSaldoRequest(BigDecimal nuevoMontoUtilizado) {}
