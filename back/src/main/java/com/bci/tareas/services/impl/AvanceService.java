package com.bci.tareas.services.impl;
import com.bci.tareas.controllers.ControllerConsulta;
import com.bci.tareas.dto.SolicitudAvanceDTO;
import com.bci.tareas.exception.SobreGiroException;
import com.bci.tareas.model.AvanceCuota;
import com.bci.tareas.model.AvanceDeuda;
import com.bci.tareas.model.CuentaBancaria;
import com.bci.tareas.model.SobreGiro;
import com.bci.tareas.model.enums.EstadoCuota;
import com.bci.tareas.repositorio.AvanceDeudaRepository;
import com.bci.tareas.repositorio.CuentaBancariaRepository;
import com.bci.tareas.repositorio.SobreGiroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import lombok.*;
@Service
@RequiredArgsConstructor
public class AvanceService {

    private final SobreGiroRepository sobreGiroRepository;
    private final AvanceDeudaRepository avanceDeudaRepository;
    private final CuentaBancariaRepository cuentaBancariaRepository;
    private static final Logger logger = LoggerFactory.getLogger(AvanceService.class);
    @Transactional // FUNDAMENTAL: Si ocurre un error, hace Rollback automático de todo
    public AvanceDeuda procesarSolicitudAvance(SolicitudAvanceDTO solicitud) {

        // 1. Buscar el sobregiro y validar
        SobreGiro sobregiro = sobreGiroRepository.findById(solicitud.idCuenta())
                .orElseThrow(() -> new SobreGiroException("Sobregiro no encontrado"));

        if (sobregiro.getDisponible().compareTo(solicitud.montoSolicitado()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para el avance solicitado.");
        }

        // 2. Restar el saldo disponible en la tabla sobregiro
        sobregiro.setDisponible(sobregiro.getDisponible().subtract(solicitud.montoSolicitado()));
        sobreGiroRepository.save(sobregiro);

        // NUEVO PASO 2.5: Sumar el dinero a la cuenta bancaria del usuario
        // Asumiendo que el 'sobregiro' tiene la relación con la 'cuentaBancaria'
        CuentaBancaria cuenta = sobregiro.getCuentaBancaria();
        cuenta.setSaldoActual(cuenta.getSaldoActual().add(solicitud.montoSolicitado()));
        cuentaBancariaRepository.save(cuenta);


        // 3. Crear el encabezado de la Deuda
        AvanceDeuda avance = new AvanceDeuda();
        avance.setSobreGiro(sobregiro);
        avance.setMontoSolicitado(solicitud.montoSolicitado());
        avance.setTasaInteres(sobregiro.getTasaInteres()); // Asumimos que viene del sobregiro
        avance.setCantidadCuotas(solicitud.cantidadCuotas());

        // 4. Calcular el calendario de cuotas (Sistema de Amortización Francés)
        generarCalendarioCuotas(avance, solicitud.montoSolicitado(), sobregiro.getTasaInteres(), solicitud.cantidadCuotas());
        logger.debug("avance",avance );
        // 5. Guardar el avance (Como tiene CascadeType.ALL, guardará las cuotas automáticamente)
        return avanceDeudaRepository.save(avance);
    }

    private void generarCalendarioCuotas(AvanceDeuda avance, BigDecimal capitalPrestado, BigDecimal tasaInteresAnual, int numCuotas) {
        // Convertir tasa anual (ej: 15.0) a tasa mensual decimal (ej: 0.0125)
        BigDecimal tasaMensual = tasaInteresAnual.divide(BigDecimal.valueOf(1200), 6, RoundingMode.HALF_UP);

        // Fórmula cuota fija: C = P * (i * (1+i)^n) / ((1+i)^n - 1)
        BigDecimal factor = tasaMensual.add(BigDecimal.ONE).pow(numCuotas);
        BigDecimal dividendo = tasaMensual.multiply(factor);
        BigDecimal divisor = factor.subtract(BigDecimal.ONE);

        BigDecimal montoCuota = capitalPrestado.multiply(dividendo).divide(divisor, 2, RoundingMode.HALF_UP);

        BigDecimal saldoRestante = capitalPrestado;
        BigDecimal totalDeudaCalculada = BigDecimal.ZERO;

        for (int i = 1; i <= numCuotas; i++) {
            BigDecimal interesMes = saldoRestante.multiply(tasaMensual).setScale(2, RoundingMode.HALF_UP);
            BigDecimal capitalMes = montoCuota.subtract(interesMes);
            saldoRestante = saldoRestante.subtract(capitalMes);

            AvanceCuota cuota = new AvanceCuota();
            cuota.setNumeroCuota(i);
            cuota.setMontoCuota(montoCuota);
            cuota.setCapital(capitalMes);
            cuota.setInteres(interesMes);
            cuota.setFechaVencimiento(LocalDate.now().plusMonths(i));
            cuota.setEstado(EstadoCuota.PENDIENTE);
            cuota.setSaldoPendiente(BigDecimal.ZERO);
            avance.addCuota(cuota);
            totalDeudaCalculada = totalDeudaCalculada.add(montoCuota);
        }

        avance.setMontoTotalDeuda(totalDeudaCalculada);
    }
}