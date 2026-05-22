package com.bci.tareas.services.impl;

import com.bci.tareas.model.HistorialEstadoUsuario;
import com.bci.tareas.model.Usuario;
import com.bci.tareas.model.enums.EstadoUsuario;
import com.bci.tareas.repositorio.HistorialEstadoUsuarioRepository;
import com.bci.tareas.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioHistorialService {

    @Autowired
    private HistorialEstadoUsuarioRepository historialRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * MÉTODO 1: El enfoque tradicional (Recomendado si necesitas validar datos del usuario)
     */
    @Transactional
    public HistorialEstadoUsuario guardarHistorial(Long idUsuario, EstadoUsuario nuevoEstado, String observacion) {

        // 1. Buscamos al usuario en la base de datos (Ejecuta un SELECT)
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario con ID " + idUsuario + " no existe."));

        // 2. Instanciamos el historial usando el constructor práctico que creamos
        HistorialEstadoUsuario historial = new HistorialEstadoUsuario(usuario, nuevoEstado, observacion);

        // 3. Guardamos el historial (Ejecuta un INSERT)
        return historialRepository.save(historial);
    }

    /**
     * MÉTODO 2: El enfoque de Alto Rendimiento (Arquitectura Optimizada)
     * Usa este si solo quieres insertar el historial sin hacer un SELECT previo.
     */
    @Transactional
    public HistorialEstadoUsuario guardarHistorialRapido(Long idUsuario, EstadoUsuario nuevoEstado, String observacion) {

        // getReferenceById() NO hace un SELECT a la base de datos.
        // Solo crea un "objeto falso" (Proxy) en memoria de Java que contiene el ID.
        // Esto ahorra tiempo de procesamiento.
        Usuario usuarioProxy = usuarioRepository.findById(idUsuario).orElseThrow(() -> new IllegalArgumentException("El usuario con ID " + idUsuario + " no existe."));
        ;
        HistorialEstadoUsuario historial = new HistorialEstadoUsuario();
        historial.setUsuario(usuarioProxy);
        historial.setEstado(nuevoEstado);
        historial.setObservacion(observacion);

        // Al guardar, Hibernate extraerá el ID del Proxy y armará el INSERT perfecto.
        return historialRepository.save(historial);
    }
}