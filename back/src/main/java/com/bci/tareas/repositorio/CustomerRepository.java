package com.bci.tareas.repositorio;


import org.springframework.data.repository.CrudRepository;

import com.bci.tareas.model.CustomerEntity;

import java.math.BigInteger;
import java.util.Optional;

public interface CustomerRepository extends CrudRepository<CustomerEntity, BigInteger> {

    Optional<CustomerEntity> findByEmail(String email);
}
