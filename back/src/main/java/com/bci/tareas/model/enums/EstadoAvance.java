package com.bci.tareas.model.enums;

public enum EstadoAvance {

    /**
     * El avance fue aprobado, el dinero fue entregado al cliente
     * y el calendario de pagos está transcurriendo normalmente.
     */
    VIGENTE,

    /**
     * El cliente se retrasó en el pago de una o más cuotas.
     * Este estado suele activarse mediante un proceso automático (Cron Job)
     * que revisa las fechas de vencimiento todos los días a la medianoche.
     */
    MOROSO,

    /**
     * Todas las cuotas del avance han sido pagadas en su totalidad.
     * El contrato está cerrado.
     */
    PAGADO,

    /**
     * (Opcional) En caso de que un ejecutivo deba reversar el avance
     * por fraude o error en el sistema antes de que se cobre la primera cuota.
     */
    ANULADO
}