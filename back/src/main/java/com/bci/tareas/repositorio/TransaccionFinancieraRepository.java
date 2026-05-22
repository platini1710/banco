package com.bci.tareas.repositorio;
import com.bci.tareas.model.TransaccionFinanciera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionFinancieraRepository extends JpaRepository<TransaccionFinanciera, String> {

    // Spring Data crea la consulta SQL automáticamente solo con leer el nombre del método.
    // Esto será útil para saber cuánto ha gastado una cuenta.
    long countByCuentaOrigen(String cuentaOrigen);
}
