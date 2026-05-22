package com.bci.tareas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_cuenta")
@Getter // Genera todos los getters
@Setter // Genera todos los setters
@NoArgsConstructor // Genera el constructor vacío (obligatorio para JPA)
@AllArgsConstructor // Genera el constructor con todos los campos
@Builder // Te permite crear objetos así: Usuario.builder().id("1").name("Juan").build()
public class TipoCuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_cuenta")
    private Integer idTipoCuenta;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;


    public TipoCuenta(String cuentaCorriente, String cuentaDeLiquidezInmediata) {
    }
}