package com.bci.tareas.model;

import com.bci.tareas.model.enums.EstadoUsuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estado_usuario")
public class HistorialEstadoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Integer idHistorial;

    // Relación de Llave Foránea: Muchos historiales pertenecen a Un usuario
    // FetchType.LAZY es una excelente práctica de rendimiento para no cargar
    // todos los datos del usuario si solo quieres ver el estado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "ENUM('ACTIVO','RETIRADO','REINCORPORADO') DEFAULT 'ACTIVO'")
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    // Delegamos la inserción de la fecha a MySQL
    @Column(name = "fecha_cambio", updatable = false, insertable = false)
    private LocalDateTime fechaCambio;

    // Mapeamos el campo TEXT de MySQL
    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    // Constructor vacío
    public HistorialEstadoUsuario() {
    }

    // Constructor práctico para cuando insertes desde tu código
    public HistorialEstadoUsuario(Usuario usuario, EstadoUsuario estado, String observacion) {
        this.usuario = usuario;
        this.estado = estado;
        this.observacion = observacion;
    }

    // Getters y Setters
    public Integer getIdHistorial() { return idHistorial; }
    public void setIdHistorial(Integer idHistorial) { this.idHistorial = idHistorial; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public EstadoUsuario getEstado() { return estado; }
    public void setEstado(EstadoUsuario estado) { this.estado = estado; }

    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}