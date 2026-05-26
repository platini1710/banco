package com.bci.tareas.model;



import com.bci.tareas.model.enums.EstadoCuentaBancaria;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cuenta_bancaria", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cuenta_banco", columnNames = {"numero_cuenta", "id_banco"})
})

@Getter // Genera todos los getters
@Setter // Genera todos los setters
@NoArgsConstructor // Genera el constructor vacío (obligatorio para JPA)
@AllArgsConstructor // Genera el constructor con todos los campos
@Builder
public class CuentaBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    private Long idCuenta;

    // --- LLAVES FORÁNEAS (Relaciones ManyToOne) ---

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @NotNull(message = "El banco es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_banco", nullable = false)
    private Banco banco;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_cuenta", nullable = false)
    private TipoCuenta tipoCuenta;

    // --- CAMPOS PROPIOS DE LA CUENTA ---

    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(max = 50, message = "El número de cuenta no puede superar los 50 caracteres")
    @Column(name = "numero_cuenta", nullable = false, length = 50)
    private String numeroCuenta;

    // BigDecimal es obligatorio en sistemas financieros para no perder precisión en los centavos
    @Column(name = "saldo_actual", precision = 15, scale = 2)
    private BigDecimal saldoActual = BigDecimal.ZERO;

    @Size(max = 3, message = "La moneda debe tener un máximo de 3 caracteres (Ej: CLP, USD)")
    @Column(name = "moneda", length = 3)
    private String moneda = "CLP";

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "ENUM('ACTIVA','BLOQUEADA','CERRADA') DEFAULT 'ACTIVA'")
    private EstadoCuentaBancaria estado = EstadoCuentaBancaria.ACTIVA;

    @Column(name = "fecha_apertura", updatable = false, insertable = false)
    private LocalDateTime fechaApertura;
}