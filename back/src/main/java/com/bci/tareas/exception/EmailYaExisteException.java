package com.bci.tareas.exception;

public class EmailYaExisteException extends RuntimeException{

    public EmailYaExisteException(String message) {
        super(message);
    }
}
