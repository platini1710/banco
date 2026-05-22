package com.bci.tareas.dto;

import com.bci.tareas.model.Banco;
import com.bci.tareas.model.TipoCuenta;
import com.bci.tareas.model.Usuario;
import com.bci.tareas.model.enums.EstadoCuentaBancaria;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CuentaBancariaDTO( Long idCuenta, Long  IdUsuario,
                                 Integer idBanco , Integer idTipoCuenta,
                                 String numeroCuenta, BigDecimal saldoActual,
                                 String moneda, EstadoCuentaBancaria estado ,
                                 LocalDateTime fechaApertura) {
}
