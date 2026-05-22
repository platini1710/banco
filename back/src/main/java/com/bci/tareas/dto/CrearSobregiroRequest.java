package com.bci.tareas.dto;

import java.math.BigDecimal;

public record CrearSobregiroRequest(Long idCuenta, BigDecimal montoAprobado, BigDecimal tasaInteres) {


}