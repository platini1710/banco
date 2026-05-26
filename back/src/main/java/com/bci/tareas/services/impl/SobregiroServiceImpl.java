package com.bci.tareas.services.impl;

import com.bci.tareas.dto.ActualizarCupoRequest;
import com.bci.tareas.dto.CrearSobregiroRequest;
import com.bci.tareas.dto.SobregiroDTO;
import com.bci.tareas.dto.UsoSobregiroRequest;
import com.bci.tareas.exception.SobregiroExcedidoException;
import com.bci.tareas.model.CuentaBancaria;
import com.bci.tareas.model.SobreGiro;
import com.bci.tareas.model.enums.EstadoSobregiro;
import com.bci.tareas.repositorio.CuentaBancariaRepository;
import com.bci.tareas.repositorio.SobreGiroRepository;
import com.bci.tareas.services.SobregiroService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SobregiroServiceImpl implements SobregiroService {
    private static final Logger logger = LoggerFactory.getLogger(SobregiroServiceImpl.class);
    private final SobreGiroRepository sobregiroRepository;
    private final CuentaBancariaRepository cuentaBancariaRepository;

    // Constante para centralizar el nombre del circuito de Resilience4j
    private static final String RESILIENCE_INSTANCE = "sobregiroService";

    // 1. CREAR REGISTRO DE SOBREGIRO
    @Override
    @Transactional
    @CircuitBreaker(name = RESILIENCE_INSTANCE) // Si la BD se bloquea o cae, abre el circuito
    public SobreGiro crearSobregiro(CrearSobregiroRequest request) {
        if (sobregiroRepository.existsByCuentaBancaria_IdCuenta(request.idCuenta())) {
            throw new RuntimeException("La cuenta ya tiene un sobregiro asignado.");
        }

        CuentaBancaria cuenta = cuentaBancariaRepository.findById(request.idCuenta())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        SobreGiro sobregiro = new SobreGiro();
        sobregiro.setCuentaBancaria(cuenta);
        sobregiro.setMontoAprobado(request.montoAprobado());
        sobregiro.setMontoUtilizado(BigDecimal.ZERO);
        sobregiro.setTasaInteres(request.tasaInteres());
        sobregiro.setFechaAprobacion(LocalDate.now());
        sobregiro.setFechaVencimiento(LocalDate.now().plusYears(1));
        sobregiro.setEstado(EstadoSobregiro.ACTIVO);

        return sobregiroRepository.save(sobregiro);
    }

    // 2. ACTUALIZAR MONTO APROBADO
    @Override
    @Transactional
    @CircuitBreaker(name = RESILIENCE_INSTANCE)
    public SobreGiro actualizarMontoAprobado(Long idCuenta, ActualizarCupoRequest request) {
        SobreGiro sobregiro = sobregiroRepository.findByCuentaBancaria_IdCuenta(idCuenta)
                .orElseThrow(() -> new RuntimeException("Sobregiro no encontrado para esta cuenta"));

        if (request.nuevoMontoAprobado().compareTo(sobregiro.getMontoUtilizado()) < 0) {
            throw new RuntimeException("El nuevo cupo no puede ser menor a lo que el cliente ya ha utilizado.");
        }

        sobregiro.setMontoAprobado(request.nuevoMontoAprobado());
        return sobregiroRepository.save(sobregiro);
    }

    // 3. ACTUALIZAR MONTO UTILIZADO (Uso de dinero - Crítico)
    @Override
    @Transactional
    @CircuitBreaker(name = RESILIENCE_INSTANCE)
    public SobreGiro utilizarSobregiro(Long idCuenta, UsoSobregiroRequest request) {
        SobreGiro sobregiro = sobregiroRepository.findByCuentaBancaria_IdCuenta(idCuenta)
                .orElseThrow(() -> new RuntimeException("Sobregiro no encontrado"));

        if (!sobregiro.getEstado().equals(EstadoSobregiro.ACTIVO)) {
            throw new RuntimeException("La línea de sobregiro no está activa.");
        }

        BigDecimal montoDisponible = sobregiro.getMontoAprobado().subtract(sobregiro.getMontoUtilizado());
        BigDecimal montoPedido = request.montoASolicitar();

        if (montoPedido.compareTo(montoDisponible) > 0) {
            throw new RuntimeException("Fondos insuficientes en la línea de crédito.");
        }

        BigDecimal nuevoMontoUtilizado = sobregiro.getMontoUtilizado().add(montoPedido);
        sobregiro.setMontoUtilizado(nuevoMontoUtilizado);
        sobregiroRepository.save(sobregiro);

        CuentaBancaria cuenta = sobregiro.getCuentaBancaria();
        BigDecimal nuevoSaldoCuenta = cuenta.getSaldoActual().add(montoPedido);
        cuenta.setSaldoActual(nuevoSaldoCuenta);
        cuentaBancariaRepository.save(cuenta);

        return sobregiro;
    }

    // 4. CONSULTA CON FALLBACK DE SEGURIDAD
    @Override
    @Transactional(readOnly = true)
    // Agregamos el cortocircuito y definimos la estrategia de salida si la DB falla
    @CircuitBreaker(name = RESILIENCE_INSTANCE, fallbackMethod = "consultarSobregirosFallback")
    public List<SobregiroDTO> consultarSobregirosPorRut(Long rut) {
        List<SobreGiro> sobregiros = sobregiroRepository.findByCuentaBancaria_Usuario_Rut(rut);

        return sobregiros.stream()
                .map(sobregiro -> {
                    BigDecimal disponible = sobregiro.getMontoAprobado().subtract(sobregiro.getMontoUtilizado());

                    return new SobregiroDTO(
                            sobregiro.getIdSobregiro(),
                            sobregiro.getCuentaBancaria().getIdCuenta(),
                            sobregiro.getCuentaBancaria().getNumeroCuenta(),
                            sobregiro.getMontoAprobado(),
                            sobregiro.getMontoUtilizado(),
                            disponible,
                            sobregiro.getEstado().name(),
                            sobregiro.getTasaInteres()
                    );
                })
                .toList();
    }

    /**
     * MÉTODOS DE FALLBACK (Contingencia de Resiliencia)
     * ¡Ojo! Debe tener exactamente la misma firma del método original más la excepción.
     */
    public List<SobregiroDTO> consultarSobregirosFallback(Long rut, Throwable t) {
        logger.error("Cortocircuito activo en consulta RUT: {}. Base de datos inaccesible o lenta. Razón: {}", rut, t.getMessage());
        // Retornamos una lista vacía o podrías cachear en Redis para no romper el frontend del banco.
        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = RESILIENCE_INSTANCE)
    public SobreGiro encontrarSobreGiroPorIdCuenta(Long idCuenta) {
        return sobregiroRepository.findByCuentaBancaria_IdCuenta(idCuenta)
                .orElseThrow(() -> new RuntimeException("Sobregiro no encontrado"));
    }

    @Override
    @Transactional
    public SobreGiro actualizaSaldo(Long idCuenta, BigDecimal nuevoMontoUtilizado) {
        logger.info("paso 1 service");

        SobreGiro sobregiro = sobregiroRepository.findByCuentaBancaria_IdCuenta(idCuenta)
                .orElseThrow(() -> new RuntimeException("Sobregiro no encontrado"));

        logger.info("paso 2 service {}", sobregiro.getIdSobregiro());

        BigDecimal nuevoMontoProyectado = sobregiro.getMontoUtilizado().add(nuevoMontoUtilizado);

        if (nuevoMontoProyectado.compareTo(sobregiro.getMontoAprobado()) > 0) {
            throw new SobregiroExcedidoException("Sobregiro Excedido");
        }

        sobregiro.setMontoUtilizado(nuevoMontoProyectado);
        sobregiro.setDisponible(sobregiro.getMontoAprobado().subtract(sobregiro.getMontoUtilizado()));

        return sobregiroRepository.save(sobregiro);
    }
}