package com.bci.tareas.services.impl;

import com.bci.tareas.dto.PhoneDTO;
import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.exception.RecursoNoEncontradoException;
import com.bci.tareas.model.enums.EstadoActivo;
import com.bci.tareas.repositorio.UsuarioDataRestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    // Inyección por constructor (Mejor práctica frente a @Autowired en campos)
    private final UsuarioDataRestRepository usuarioDataRestRepository;

    public UserDetailsServiceImpl(UsuarioDataRestRepository usuarioDataRestRepository) {
        this.usuarioDataRestRepository = usuarioDataRestRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String rutIngresado) throws UsernameNotFoundException {
        logger.info("Intentando autenticar RUT: {}", rutIngresado);

        Long rutNumerico;
        try {
            // 1. Convertimos el String que viene del formulario de Login a Long
            // (Asumimos que el front-end o el filtro de seguridad envía el RUT sin puntos ni guión)
            rutNumerico = Long.valueOf(rutIngresado);

        } catch (NumberFormatException e) {
            logger.error("El RUT ingresado no tiene formato numérico válido: {}", rutIngresado);
            throw new UsernameNotFoundException("Formato de RUT inválido");
        }
        logger.info("✅ Usuario rutNumerico  {}",
                rutNumerico);
        // 2. Buscamos en la base de datos usando el Long
        return usuarioDataRestRepository.findByRut(rutNumerico)
                .map(user -> {
                    // 1. Log más detallado para auditoría
                    logger.info("✅ Usuario encontrado en BD - RUT: {}, Rol en BD: {}, Activo: {}",
                            user.getRut(), user.getRol(), user.getActivo());

                    // 2. Protegemos contra un Rol nulo (si es nulo, le damos "USER" por defecto)
                    String rolSeguro = (user.getRol() != null && !user.getRol().trim().isEmpty())
                            ? user.getRol().trim()
                            : "USER";
                    logger.info("✅ Usuario rolSeguro  {}",
                            rolSeguro);
                    // 3. Si en la base de datos le pusiste "ROLE_ADMIN", le quitamos el "ROLE_"
                    // para que el builder de Spring no explote.
                    if (rolSeguro.startsWith("ROLE_")) {
                        rolSeguro = rolSeguro.substring(5);
                    }

                    logger.info("✅ Usuario rolSeguro  {}",
                            rolSeguro);
                    // 4. Verificamos el estado de la cuenta
                    boolean cuentaActiva = (user.getActivo() == EstadoActivo.ACTIVO);
                    if (!cuentaActiva) {
                        logger.warn("⚠️ El usuario RUT {} está inactivo en la base de datos.", user.getRut());
                    }

                    // 5. Construimos el usuario de forma segura
                    return User.builder()
                            .username(String.valueOf(user.getRut()))
                            .password(user.getPassword())
                            .roles(rolSeguro)
                            .disabled(!cuentaActiva)
                            .build();
                })
                // 5. Excepción estándar de Spring Security
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con RUT " + rutIngresado + " no encontrado"));
    }
}