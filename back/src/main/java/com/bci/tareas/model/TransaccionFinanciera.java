package com.bci.tareas.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transaccion_financiera")
public class TransaccionFinanciera {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id; // Recomendable generar con UUID.randomUUID().toString()

    @Column(name = "cuenta_origen", length = 50, nullable = false)
    private String cuentaOrigen;

    @Column(name = "cuenta_destino", length = 50, nullable = false)
    private String cuentaDestino;

    @Column(name = "monto", precision = 15, scale = 4, nullable = false)
    private BigDecimal monto;

    @Column(name = "moneda", length = 3, nullable = false)
    private String moneda;

    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;

    @Column(name = "dispositivo_id", length = 100)
    private String dispositivoId;

    @Column(name = "estado_evaluacion", length = 30, nullable = false)
    private String estadoEvaluacion;

    // Mapeo nativo de JSON para Hibernate 6+
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload_original", columnDefinition = "json")
    private String payloadOriginal;

    // insertable y updatable en false porque la BD maneja el DEFAULT CURRENT_TIMESTAMP(3)
    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;
/*
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumns({
    @JoinColumn(name = "usuario_rut", referencedColumnName = "rut", nullable = false)
})
private Usuario usuario;
 */
    // Mapeo escalar de la FK para simplificar inserciones rápidas.
    // (Se podría cambiar a @ManyToOne si necesitas traer los datos del Usuario completo en cada consulta)
    @Column(name = "usuario_rut", nullable = false)
    private Long usuarioRut;
}