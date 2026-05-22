package com.bci.tareas.model;

import com.bci.tareas.model.enums.EstadoBanco;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "banco")
@Getter // Genera todos los getters
@Setter // Genera todos los setters
@NoArgsConstructor // Genera el constructor vacío (obligatorio para JPA)
@AllArgsConstructor // Genera el constructor con todos los campos
@Builder // Te permite crear objetos así: Banco.builder().id("1").name("BCI").build()
public class Banco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_banco")
    private Integer idBanco;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "codigo_institucion", nullable = false, unique = true, length = 20)
    private String codigoInstitucion;

    // Aquí le decimos a JPA que guarde el enum como un texto (String) en la BD,
    // no como un número, para que calce perfecto con tu ENUM('ACTIVO','INACTIVO')
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "ENUM('ACTIVO','INACTIVO') DEFAULT 'ACTIVO'")
    private EstadoBanco estado = EstadoBanco.ACTIVO; // Asignamos el valor por defecto



    public Banco(String nombre, String codigoInstitucion) {
        this.nombre = nombre;
        this.codigoInstitucion = codigoInstitucion;
    }
}
