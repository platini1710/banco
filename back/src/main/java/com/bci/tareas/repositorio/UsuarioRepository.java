package com.bci.tareas.repositorio;

import com.bci.tareas.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findById(Long id);


    // Buscar por el campo único Email (Clásico para el Login del motor de fraude)
    Optional<Usuario> findByEmail(String email);

    // Aunque el RUT es parte de la clave compuesta, puedes buscar registros usando solo el RUT
    Optional<Usuario> findByRut(Long rut);
}