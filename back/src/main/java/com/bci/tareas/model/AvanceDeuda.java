package com.bci.tareas.model;

import com.bci.tareas.model.enums.EstadoAvance;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Getter // Genera todos los getters
@Setter // Genera todos los setters
@NoArgsConstructor // Genera el constructor vacío (obligatorio para JPA)
@AllArgsConstructor // Genera el constructor con todos los campos
@Entity
@Table(name = "avance_deuda")
public class AvanceDeuda {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAvance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sobregiro")
    private SobreGiro sobreGiro;

    private BigDecimal montoSolicitado;
    private BigDecimal tasaInteres;
    private Integer cantidadCuotas;
    private BigDecimal montoTotalDeuda;

    @Enumerated(EnumType.STRING)
    private EstadoAvance estado = EstadoAvance.VIGENTE;

    // Relación OneToMany hacia las cuotas (Cascada para guardar todo junto)
    @OneToMany(mappedBy = "avanceDeuda", cascade = CascadeType.ALL)
    private List<AvanceCuota> cuotas = new ArrayList<>();

    // Getters, Setters, y método helper
    public void addCuota(AvanceCuota cuota) {
        cuotas.add(cuota);
        cuota.setAvanceDeuda(this);
    }
}