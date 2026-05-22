package com.bci.tareas.dto;

import com.bci.tareas.helper.Constantes;
import com.bci.tareas.model.Phone;
import com.bci.tareas.model.enums.EstadoActivo;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Getter
@Setter
@EqualsAndHashCode()
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioDTO {


    @NotNull(message = Constantes.ID_NULL)
    private Long idUsuario;

    @Min(value = 10000000L, message = "El RUT debe tener exactamente 8 dígitos")
    @Max(value = 99999999L, message = "El RUT debe tener exactamente 8 dígitos")
    @NotNull(message = Constantes.ID_NULL)
    private Long rut;



    @NotNull(message = Constantes.ID_NULL)
    private String dv;

    @NotBlank(message = "El nombre no puede ser nulo ni estar vacío")
    @Size(max = 40, message = "El nombre no puede superar los 40 caracteres")
    private String nombre;

    @NotBlank(message = "La contraseña no puede ser nula ni estar vacía")
    @Size(min = 5, max = 255, message = "La contraseña debe tener entre 5 y 255 caracteres")
    private String password;

    @NotBlank(message = "El email no puede ser nulo ni estar vacío")
    @Email(message = "El formato del correo electrónico no es válido (ejemplo: usuario@dominio.com)")
    private String email;

    // Insertable y updatable en false para que MySQL maneje el CURRENT_TIMESTAMP por defecto

    private LocalDateTime fechaCreacion;


    private EstadoActivo activo;


    private String rol;


    private LocalDateTime fechaModificacion ;


    private LocalDateTime fechaBaja ;

}
