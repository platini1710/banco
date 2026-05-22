package com.bci.tareas.model;

import java.io.Serializable;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "telefono")
@Getter // Genera todos los getters
@Setter // Genera todos los setters
@NoArgsConstructor // Genera el constructor vacío (obligatorio para JPA)
@AllArgsConstructor // Genera el constructor con todos los campos
@Builder // Te permite crear objetos así: Usuario.builder().id("1").name("Juan").build()
public class Phone  implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    private Long rut;
   
    @Column(name = "numero", nullable = true)
	private Long number;

    @Column(name = "codigo_pais", nullable = true)
	private String codigoPais;


    



}
