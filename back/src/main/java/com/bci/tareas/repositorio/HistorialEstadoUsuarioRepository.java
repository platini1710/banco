package com.bci.tareas.repositorio;
import com.bci.tareas.model.HistorialEstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistorialEstadoUsuarioRepository extends JpaRepository<HistorialEstadoUsuario, Integer> {

    // Busca todo el historial de un usuario específico usando su ID
    // El "OrderByFechaCambioDesc" le dice a JPA que agregue "ORDER BY fecha_cambio DESC" al SQL final
    List<HistorialEstadoUsuario> findByUsuario_IdUsuarioOrderByFechaCambioDesc(Integer idUsuario);

    // Otra opción: buscar todo el historial asociado directamente al RUT del usuario
    List<HistorialEstadoUsuario> findByUsuario_RutOrderByFechaCambioDesc(Long rut);


}