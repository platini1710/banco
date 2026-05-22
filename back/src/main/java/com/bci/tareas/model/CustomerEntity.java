package com.bci.tareas.model;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "customers")
@Data
public class CustomerEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String email;
    @Column(name = "pwd")
    private  String password;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_customer")
    private List<RoleEntity> roles;
}
