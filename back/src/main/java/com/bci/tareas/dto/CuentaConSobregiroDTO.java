package com.bci.tareas.dto;


import com.bci.tareas.model.enums.EstadoCuentaBancaria;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

import java.time.LocalDateTime;


@Getter
@Setter
@EqualsAndHashCode()
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaConSobregiroDTO {
    private Long idCuenta;
    private Long idUsuario;
    private Integer idBanco;
    private Integer idTipoCuenta;
    private String numeroCuenta;
    private BigDecimal saldoActual;
    private String moneda;
    private EstadoCuentaBancaria estado;
    private LocalDateTime fechaApertura;
    private BigDecimal disponible;
    private BigDecimal montoUtilizado;



    public CuentaConSobregiroDTO(Long idCuenta,Long idUsuario,  Integer idBanco
            ,Integer idTipoCuenta,String numeroCuenta,BigDecimal saldoActual,
                                 String moneda,EstadoCuentaBancaria estado,
                                 BigDecimal disponible,BigDecimal montoUtilizado) {
        this.idCuenta=idCuenta;
        this.idUsuario=idUsuario;
        this.idBanco=idBanco;
        this.idTipoCuenta=idTipoCuenta;
        this.numeroCuenta=numeroCuenta;
        this.saldoActual=saldoActual;
        this.moneda=moneda;
        this.estado=estado;
        this.disponible=disponible;
        this.montoUtilizado=montoUtilizado;
    }
}