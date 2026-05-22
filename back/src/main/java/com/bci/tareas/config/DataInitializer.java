package com.bci.tareas.config;

import com.bci.tareas.repositorio.UsuarioDataRestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioDataRestRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioDataRestRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Buscamos tu usuario por el RUT
        usuarioRepository.findById(11566187L).ifPresent(user -> {

            // Encriptamos la palabra "admin" usando el motor oficial de tu Spring
            String hashPerfecto = passwordEncoder.encode("admin");

            // Actualizamos la clave y la guardamos en la BD
            user.setPassword(hashPerfecto);
            usuarioRepository.save(user);

            System.out.println("🔥 CLAVE ACTUALIZADA A BCRYPT DIRECTO DESDE SPRING 🔥");
            System.out.println("Hash generado: " + hashPerfecto);
        });
    }
}