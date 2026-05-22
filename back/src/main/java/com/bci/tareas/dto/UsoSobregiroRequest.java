package com.bci.tareas.dto;

import java.math.BigDecimal;

// Para cuando el usuario hace clic en "Transferir" en React
public record UsoSobregiroRequest(BigDecimal montoASolicitar) {}