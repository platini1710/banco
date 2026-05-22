package com.bci.tareas.repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bci.tareas.model.Phone;
@Repository 
public interface PhoneDataRestRepository extends CrudRepository<Phone, Long>{
	Phone save(Phone phone);
}
