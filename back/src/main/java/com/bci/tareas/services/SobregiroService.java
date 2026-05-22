package com.bci.tareas.services;

import com.bci.tareas.dto.ActualizarCupoRequest;
import com.bci.tareas.dto.CrearSobregiroRequest;
import com.bci.tareas.dto.SobregiroDTO;
import com.bci.tareas.dto.UsoSobregiroRequest;
import com.bci.tareas.model.SobreGiro;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface SobregiroService {
     SobreGiro crearSobregiro(CrearSobregiroRequest request) ;

    // 2. ACTUALIZAR MONTO APROBADO (Aumento o rebaja de cupo)
    @Transactional
    SobreGiro actualizarMontoAprobado(Long idCuenta, ActualizarCupoRequest request);

    // 3. ACTUALIZAR MONTO UTILIZADO (Cuando el usuario pide plata en el frontend)
    @Transactional // CRÍTICO: Si falla guardar la cuenta, no se guarda el sobregiro (Rollback)
    SobreGiro utilizarSobregiro(Long idCuenta, UsoSobregiroRequest request);

    // ... (Tus otros métodos de crear y utilizar sobregiro) ...
    @Transactional(readOnly = true) // readOnly = true optimiza el rendimiento para consultas de solo lectura
    List<SobregiroDTO> consultarSobregirosPorRut(Long rut);

    @Transactional(readOnly = true)
    SobreGiro encontrarSobreGiroPorIdCuenta(Long idCuenta);

    @Transactional(readOnly = false)
    SobreGiro actualizaSaldo(Long idCuenta, BigDecimal nuevoMontoUtilizado);
}
