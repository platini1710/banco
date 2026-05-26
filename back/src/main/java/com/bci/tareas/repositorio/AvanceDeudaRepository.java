package com.bci.tareas.repositorio;

import com.bci.tareas.model.AvanceDeuda;
import com.bci.tareas.model.enums.EstadoAvance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvanceDeudaRepository extends JpaRepository<AvanceDeuda, Long> {

    /**
     * Devuelve el historial completo de todos los avances (créditos)
     * que ha solicitado una cuenta en particular.
     * Ideal para la vista de "Historial de Créditos" en el frontend.
     */
    List<AvanceDeuda> findBySobreGiro_IdSobregiro(Long idSobregiro);

    /**
     * Devuelve todos los avances que se encuentren en un estado específico.
     * Ideal para un proceso automático (Cron Job) que busque todos los
     * avances 'MOROSOS' para enviar correos de cobranza.
     */
    List<AvanceDeuda> findByEstado(EstadoAvance estado);

    /**
     * Devuelve los avances de un sobregiro filtrando por su estado.
     * Ideal para mostrarle al cliente en su dashboard web solo los
     * créditos que aún tiene activos (VIGENTES o MOROSOS).
     */
    List<AvanceDeuda> findBySobreGiro_IdSobregiroAndEstado(Long idSobregiro, EstadoAvance estado);

}