package com.bci.tareas.repositorio;



import com.bci.tareas.model.CuentaBancaria;
import com.bci.tareas.model.enums.EstadoCuentaBancaria;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, Long> {

    // 1. Obtener todas las cuentas asociadas a un usuario específico (usando su ID)
    List<CuentaBancaria> findByUsuario_IdUsuario(Long idUsuario);

    // 2. Obtener todas las cuentas de un RUT específico (Ideal para reportes de fraude)
    List<CuentaBancaria> findByUsuario_Rut(Long rut);

    // 3. Buscar una cuenta exacta usando la restricción UNIQUE de tu base de datos
    Optional<CuentaBancaria> findByNumeroCuentaAndBanco_IdBanco(String numeroCuenta, Integer idBanco);

    // 4. Buscar cuentas bloqueadas de un usuario (Para alertas tempranas)
    List<CuentaBancaria> findByUsuario_IdUsuarioAndEstado(Long idUsuario, EstadoCuentaBancaria estado);



    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CuentaBancaria c WHERE c.idCuenta = :idCuenta")
    Optional<CuentaBancaria> findByIdConBloqueo(@Param("idCuenta") Long idCuenta);

    @Modifying
    @Query("UPDATE CuentaBancaria c SET c.saldoActual = c.saldoActual - :monto WHERE c.idCuenta = :idCuenta")
    int restarSaldo(@Param("idCuenta") Long idCuenta, @Param("monto") BigDecimal monto);
}