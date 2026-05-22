package com.bci.tareas.repositorio;
import com.bci.tareas.model.TipoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TipoCuentaRepository extends JpaRepository<TipoCuenta, Integer> {

    // Spring Boot es tan inteligente que si nombras el método así,
    // automáticamente creará la query: SELECT * FROM tipo_cuenta WHERE nombre = ?
    Optional<TipoCuenta> findByNombre(String nombre);

}