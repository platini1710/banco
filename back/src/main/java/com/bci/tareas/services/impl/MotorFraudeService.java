package com.bci.tareas.services.impl;

import com.bci.tareas.model.TransaccionFinanciera;
import com.bci.tareas.repositorio.TransaccionFinancieraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class MotorFraudeService {

    private final TransaccionFinancieraRepository repository;

    // Inyección de dependencias por constructor (Buena práctica)
    public MotorFraudeService(TransaccionFinancieraRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TransaccionFinanciera procesarTransaccion(TransaccionFinanciera tx) {
        // 1. Asignar reglas iniciales
        tx.setEstadoEvaluacion("EN_EVALUACION");

        // 2. Aquí iría la lógica pesada:
        // Llamar a listas negras, verificar montos, consultar APIs externas.
        // (En un entorno real, esto se ejecutaría en paralelo usando hilos virtuales)
        boolean esSospechosa = evaluarReglasDeNegocio(tx);

        // 3. Decidir el estado final
        if (esSospechosa) {
            tx.setEstadoEvaluacion("BLOQUEADA");
        } else {
            tx.setEstadoEvaluacion("APROBADA");
        }

        // 4. Guardar en tu base de datos MySQL (MV1)
        return repository.save(tx);
    }

    private boolean evaluarReglasDeNegocio(TransaccionFinanciera tx) {
        // Lógica dummy por ahora: Bloquear todo lo mayor a 10,000
        return tx.getMonto().doubleValue() > 10000.00;
    }
}