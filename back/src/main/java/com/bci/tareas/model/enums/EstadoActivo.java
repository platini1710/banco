package com.bci.tareas.model.enums;

public enum EstadoActivo {
    INACTIVO(0),
    ACTIVO(1);

    private final Integer valorDb;

    EstadoActivo(Integer valorDb) {
        this.valorDb = valorDb;
    }

    public Integer getValorDb() {
        return valorDb;
    }
}