package com.bci.tareas.dto;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@EqualsAndHashCode()
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvanceRequestDTO {
    private Long idCuenta;
    private BigDecimal monto;
    // Getters y Setters
}