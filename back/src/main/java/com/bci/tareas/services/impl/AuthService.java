package com.bci.tareas.services.impl;

import com.bci.tareas.repositorio.UsuarioDataRestRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioDataRestRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Inyección de dependencias por constructor
    public AuthService(UsuarioDataRestRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Valida si la contraseña ingresada coincide con el hash de la base de datos.
     *
     * @param id El identificador (o username) del usuario.
     * @param passwordPlana La contraseña tal como la escribió el usuario en el login.
     * @return true si coinciden, false si la contraseña es incorrecta o el usuario no existe.
     */
    public boolean validarCredenciales(Long id, String passwordPlana) {
        // 1. Buscamos al usuario SOLO por su ID
        return usuarioRepository.findById(id)
                // 2. Si el usuario existe, extraemos su password (que es un hash BCrypt)
                // y usamos matches() para que Spring Security haga la magia criptográfica
                .map(usuario -> passwordEncoder.matches(passwordPlana, usuario.getPassword()))
                // 3. Si el usuario no existe en la base de datos, retornamos false
                .orElse(false);
    }
}