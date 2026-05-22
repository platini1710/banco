package com.bci.tareas.model;

import com.bci.tareas.model.enums.EstadoSobregiro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sobregiro")
@Data // Anotación de Lombok para generar Getters y Setters automáticamente
@NoArgsConstructor
@AllArgsConstructor
public class SobreGiro   implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sobregiro")
    private Long idSobregiro;

    // Relación 1 a 1: Una cuenta bancaria tiene una línea de sobregiro
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuenta", nullable = false, unique = true)
    private CuentaBancaria cuentaBancaria;

    @Column(name = "monto_aprobado", nullable = false, precision = 15, scale = 2)
    private BigDecimal montoAprobado;

    @Column(name = "monto_utilizado", precision = 15, scale = 2)
    private BigDecimal montoUtilizado;

    @Column(name = "tasa_interes", nullable = false, precision = 5, scale = 2)
    private BigDecimal tasaInteres;

    @Column(name = "fecha_aprobacion", nullable = false)
    private LocalDate fechaAprobacion;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private EstadoSobregiro estado;
    @Column(name = "disponible", nullable = false, precision = 15, scale = 2)
    private BigDecimal disponible;
}