package com.bci.tareas.model;

import com.bci.tareas.model.enums.EstadoActivo;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "usuario")

@Getter // Genera todos los getters
@Setter // Genera todos los setters
@NoArgsConstructor // Genera el constructor vacío (obligatorio para JPA)
@AllArgsConstructor // Genera el constructor con todos los campos
@Builder // Te permite crear objetos así: Usuario.builder().id("1").name("Juan").build()
public class Usuario {

    @Id // ¡Ahora es el único @Id, adiós al @IdClass!
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "rut", nullable = false, unique = true) // El RUT sigue siendo único y obligatorio
    private Long rut;



    @Column(name = "dv", length = 1)
    private String dv;

    @Column(name = "nombre", length = 40)
    private String nombre;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    // Insertable y updatable en false para que MySQL maneje el CURRENT_TIMESTAMP por defecto
    @Column(name = "fecha_creacion", updatable = false, insertable = false)
    private LocalDateTime fechaCreacion;

    // tinyint(1) se mapea de forma nativa e ideal como Boolean
    @Column(name = "activo")
    private EstadoActivo activo;

    @Column(name = "rol", length = 5)
    private String rol;

    @Column(name = "fecha_modificacion", nullable = false)
    private LocalDateTime fechaModificacion = LocalDateTime.now();

    @Column(name = "fecha_baja", nullable = false)
    private LocalDateTime fechaBaja = null;
}
