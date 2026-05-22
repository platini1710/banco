package com.bci.tareas.repositorio;

import com.bci.tareas.model.Banco;
import com.bci.tareas.model.enums.EstadoBanco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Integer> {

    // Consulta automática por el código oficial (que es UNIQUE)
    Optional<Banco> findByCodigoInstitucion(String codigoInstitucion);

    // Consulta automática para obtener todos los bancos que están activos
    List<Banco> findByEstado(EstadoBanco estado);
}