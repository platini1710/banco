package com.bci.tareas.model.enums;
public enum EstadoCuota {
    PENDIENTE,
    PAGADA_PARCIALMENTE,
    PAGADA,
    VENCIDA // O MOROSA, cuando pasa la fecha de vencimiento y no se ha pagado
}