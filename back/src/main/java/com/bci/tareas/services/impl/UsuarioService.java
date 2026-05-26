package com.bci.tareas.services.impl;
import com.bci.tareas.model.HistorialEstadoUsuario;
import com.bci.tareas.model.Usuario;
import com.bci.tareas.model.enums.EstadoActivo;
import com.bci.tareas.repositorio.HistorialEstadoUsuarioRepository;
import com.bci.tareas.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bci.tareas.model.enums.EstadoUsuario;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class UsuarioService {

    // Inyectamos AMBOS repositorios en este servicio
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HistorialEstadoUsuarioRepository historialRepository;

    /**
     * El @Transactional asegura que si ocurre CUALQUIER error durante el método,
     * no se guarda ni el usuario ni el historial (Rollback automático).
     */
    @Transactional(rollbackFor = Exception.class)
    public Usuario registrarNuevoUsuario(Usuario nuevoUsuario, String observacionInicial) {

        // PASO 1: Por regla de negocio de tu tabla, al nacer el usuario está activo
        nuevoUsuario.setActivo(EstadoActivo.ACTIVA);

        // PASO 2: Guardamos el Usuario.
        // IMPORTANTE: Al hacer el save(), Spring Data y MySQL le asignan el ID
        // autoincremental al objeto automáticamente.
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        // PASO 3: Creamos el objeto del Historial.
        // Le pasamos el 'usuarioGuardado' que ahora ya tiene su ID real.
        HistorialEstadoUsuario historial = new HistorialEstadoUsuario(
                usuarioGuardado,
                EstadoUsuario.ACTIVO, // El estado inicial
                observacionInicial    // Ej: "Creación de cuenta por registro web"
        );

        // PASO 4: Guardamos el Historial
        historialRepository.save(historial);

        // Retornamos el usuario creado por si el Controlador lo necesita
        return usuarioGuardado;
    }
    @PostMapping("/buscar-id")
    public ResponseEntity<Long> findUsuarioId(@RequestBody Long rutBuscado) {


        // Buscamos el Optional<Usuario> en la base de datos
        Optional<Usuario> usuarioGuardado = usuarioRepository.findByRut(rutBuscado);

        // Usamos .map() para transformar el Optional<Usuario> en un Optional<Integer>
        return usuarioGuardado
                .map(usuario -> ResponseEntity.ok(usuario.getIdUsuario())) // Si existe, extraemos el ID y devolvemos 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build());       // Si no existe, devolvemos 404 Not Found
    }

}