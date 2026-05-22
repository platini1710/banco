package com.bci.tareas.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;


public class RecursoNoEncontradoException extends RuntimeException {

	  /**
	   * Instantiates a new Resource not found exception.
	   *
	   * @param message the message
	   */

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

}