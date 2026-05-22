package com.bci.tareas.exception;

import com.bci.tareas.helper.Constantes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(RecursoNoEncontradoException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", "Recurso no encontrado");


        return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND); // 404 Bad Request.
    }

    @ExceptionHandler(RecursoYaExisteException.class)
    public ResponseEntity<Map<String, String>> handleNotExiste(RecursoYaExisteException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", Constantes.YA_EXISTE);


        return new ResponseEntity<>(respuesta, HttpStatus.CONFLICT); // 404 Bad Request.
    }

    // 2. NUEVO Manejador para Correo Ya Existe (409 Conflict)
    @ExceptionHandler(EmailYaExisteException.class)
    public ResponseEntity<Map<String, String>> handleEmailConflict(EmailYaExisteException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("Error", ex.getMessage());
        // Usamos CONFLICT (409) para duplicados
        return new ResponseEntity<>(respuesta, HttpStatus.CONFLICT);
    }

    // 1. Maneja cuando la contraseña está mal o el usuario no existe
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleBadCredentials(Exception ex) {
        Map<String, String> response = new HashMap<>();

        // El mensaje seguro estándar de la industria
        response.put("error", "Unauthorized");
        response.put("mensaje", "RUT o Clave de internet incorrectos.");

        // Devolvemos un código HTTP 401 (No Autorizado)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }


    @ExceptionHandler(SobregiroExcedidoException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(SobregiroExcedidoException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", "REGLA_DE_NEGOCIO");
        respuesta.put("mensaje", ex.getMessage());
        return new ResponseEntity<>(respuesta, HttpStatus.UNPROCESSABLE_ENTITY); // 404 Bad Request.
    }

}
