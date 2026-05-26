package com.bci.tareas.model;

import com.bci.tareas.model.enums.EstadoCuota;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter // Genera todos los getters
@Setter // Genera todos los setters
@NoArgsConstructor // Genera el constructor vacío (obligatorio para JPA)
@AllArgsConstructor // Genera el constructor con todos los campos
@Entity
@Table(name = "avance_cuota")
public class AvanceCuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuota")
    private Long idCuota;

    // Relación Muchos a Uno: Muchas cuotas pertenecen a una sola Deuda (Avance)
    // FetchType.LAZY es vital para no cargar toda la deuda y el sobregiro si solo queremos ver la cuota
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avance", nullable = false)
    private AvanceDeuda avanceDeuda;

    @Column(name = "numero_cuota", nullable = false)
    private Integer numeroCuota; // Ej: 1, 2, 3...

    @Column(name = "monto_cuota", nullable = false, precision = 15, scale = 2)
    private BigDecimal montoCuota;

    @Column(name = "capital", nullable = false, precision = 15, scale = 2)
    private BigDecimal capital; // Parte que amortiza la deuda real

    @Column(name = "interes", nullable = false, precision = 15, scale = 2)
    private BigDecimal interes; // Ganancia del banco

    // Campo clave para permitir pagos parciales
    @Column(name = "saldo_pendiente", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoPendiente;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCuota estado = EstadoCuota.PENDIENTE;

    // ====================================================================
    // MÉTODOS DE NEGOCIO (Domain-Driven Design)
    // ====================================================================

    /**
     * Método para rebajar el saldo cuando el cliente hace un pago.
     * Mantiene la lógica de estados encapsulada dentro de la misma cuota.
     */
    public void registrarAbono(BigDecimal abono) {
        this.saldoPendiente = this.saldoPendiente.subtract(abono);

        // Si el saldo pendiente es 0 o menor, la cuota está pagada
        if (this.saldoPendiente.compareTo(BigDecimal.ZERO) <= 0) {
            this.estado = EstadoCuota.PAGADA;
            // Opcional: Si el saldo quedó negativo (pagó de más), podrías manejar un "vuelto" o saldo a favor aquí.
        } else {
            this.estado = EstadoCuota.PAGADA_PARCIALMENTE;
        }
    }

}