package com.bci.tareas.repositorio;

import com.bci.tareas.model.SobreGiro;
import com.bci.tareas.model.SobreGiro;
import com.bci.tareas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SobreGiroRepository extends JpaRepository<SobreGiro, Long> {

    // Spring Data JPA crea la query automáticamente para buscar por el ID de la cuenta
    Optional<SobreGiro> findByCuentaBancaria_IdCuenta(Long idCuenta);
    Optional<SobreGiro> findById(Long idCuenta);
    boolean existsByCuentaBancaria_IdCuenta(Long idCuenta);
    // NUEVO: Busca todos los sobregiros navegando desde la Cuenta hasta el RUT del Usuario
    List<SobreGiro> findByCuentaBancaria_Usuario_Rut(Long rut);


}