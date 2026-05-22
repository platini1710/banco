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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor // ¡Recuerda! Esto exige que inyectemos con 'final'
public class SobregiroServiceImpl implements SobregiroService {
    private static final Logger logger = LoggerFactory.getLogger(SobregiroServiceImpl.class);
    private final SobreGiroRepository sobregiroRepository;
    private final CuentaBancariaRepository cuentaBancariaRepository; // Asumo que ya tienes este

    // 1. CREAR REGISTRO DE SOBREGIRO
    @Override
    @Transactional
    public SobreGiro crearSobregiro(CrearSobregiroRequest request) {
        if (sobregiroRepository.existsByCuentaBancaria_IdCuenta(request.idCuenta())) {
            throw new RuntimeException("La cuenta ya tiene un sobregiro asignado.");
        }

        CuentaBancaria cuenta = cuentaBancariaRepository.findById(request.idCuenta())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        SobreGiro sobregiro = new SobreGiro();
        sobregiro.setCuentaBancaria(cuenta);
        sobregiro.setMontoAprobado(request.montoAprobado());
        sobregiro.setMontoUtilizado(BigDecimal.ZERO); // Empieza en 0
        sobregiro.setTasaInteres(request.tasaInteres());
        sobregiro.setFechaAprobacion(LocalDate.now());
        sobregiro.setFechaVencimiento(LocalDate.now().plusYears(1)); // Vence en 1 año
        sobregiro.setEstado(EstadoSobregiro.ACTIVO);

        return sobregiroRepository.save(sobregiro);
    }

    // 2. ACTUALIZAR MONTO APROBADO (Aumento o rebaja de cupo)
    @Transactional
    @Override
    public SobreGiro actualizarMontoAprobado(Long idCuenta, ActualizarCupoRequest request) {
        SobreGiro sobregiro = sobregiroRepository.findByCuentaBancaria_IdCuenta(idCuenta)
                .orElseThrow(() -> new RuntimeException("Sobregiro no encontrado para esta cuenta"));

        if (request.nuevoMontoAprobado().compareTo(sobregiro.getMontoUtilizado()) < 0) {
            throw new RuntimeException("El nuevo cupo no puede ser menor a lo que el cliente ya ha utilizado.");
        }

        sobregiro.setMontoAprobado(request.nuevoMontoAprobado());
        return sobregiroRepository.save(sobregiro);
    }

    // 3. ACTUALIZAR MONTO UTILIZADO (Cuando el usuario pide plata en el frontend)
    @Transactional
    @Override
    public SobreGiro utilizarSobregiro(Long idCuenta, UsoSobregiroRequest request) {
        SobreGiro sobregiro = sobregiroRepository.findByCuentaBancaria_IdCuenta(idCuenta)
                .orElseThrow(() -> new RuntimeException("Sobregiro no encontrado"));

        if (!sobregiro.getEstado().equals(EstadoSobregiro.ACTIVO)) {
            throw new RuntimeException("La línea de sobregiro no está activa.");
        }

        BigDecimal montoDisponible = sobregiro.getMontoAprobado().subtract(sobregiro.getMontoUtilizado());
        BigDecimal montoPedido = request.montoASolicitar();

        // Validación de negocio (Anti-fraude)
        if (montoPedido.compareTo(montoDisponible) > 0) {
            throw new RuntimeException("Fondos insuficientes en la línea de crédito.");
        }

        // Paso A: Aumentar la deuda (Monto Utilizado)
        BigDecimal nuevoMontoUtilizado = sobregiro.getMontoUtilizado().add(montoPedido);
        sobregiro.setMontoUtilizado(nuevoMontoUtilizado);
        sobregiroRepository.save(sobregiro);

        // Paso B: Depositar la plata en la cuenta corriente
        CuentaBancaria cuenta = sobregiro.getCuentaBancaria();
        BigDecimal nuevoSaldoCuenta = cuenta.getSaldoActual().add(montoPedido);
        cuenta.setSaldoActual(nuevoSaldoCuenta);
        cuentaBancariaRepository.save(cuenta);

        return sobregiro;
    }

    // ... (Tus otros métodos de crear y utilizar sobregiro) ...
    @Transactional(readOnly = true)
    @Override
    public List<SobregiroDTO> consultarSobregirosPorRut(Long rut) {

        List<SobreGiro> sobregiros = sobregiroRepository.findByCuentaBancaria_Usuario_Rut(rut);

        // Transformamos la lista de Entidades a una lista de DTOs
        return sobregiros.stream()
                .map(sobregiro -> {
                    // Calculamos el disponible aquí mismo
                    BigDecimal disponible =
                            sobregiro.getMontoAprobado().
                                    subtract(sobregiro.getMontoUtilizado());

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

    @Transactional(readOnly = true)
    @Override
    public SobreGiro encontrarSobreGiroPorIdCuenta(Long idCuenta) {

        return sobregiroRepository.findByCuentaBancaria_IdCuenta(idCuenta)
                .orElseThrow(() -> new RuntimeException("Sobregiro no encontrado"));

    }

    @Transactional(readOnly = false)
    @Override
    public SobreGiro actualizaSaldo(Long idCuenta, BigDecimal nuevoMontoUtilizado) {
logger.info(" paso 1 service ");
        // 1. Buscamos el sobregiro (Aquí sí funciona el orElseThrow porque devuelve un Optional)
        SobreGiro sobregiro = sobregiroRepository.findByCuentaBancaria_IdCuenta(idCuenta)
                .orElseThrow(() -> new RuntimeException("Sobregiro no encontrado"));
        logger.info(" paso 2 service " + sobregiro.getIdSobregiro());
        // 2. Actualizamos el valor
// 1. Es buena práctica sacar la suma a una variable aparte para que el 'if' sea legible
        BigDecimal montoUtilizado = sobregiro.getMontoUtilizado();
        BigDecimal nuevoMontoProyectado = sobregiro.getMontoUtilizado().add(nuevoMontoUtilizado);
// 2. Usamos compareTo() > 0 en lugar del símbolo >
        if (nuevoMontoProyectado.compareTo(sobregiro.getMontoAprobado()) > 0) {

            // 3. Agregamos el 'throw' para lanzar el error
            throw new SobregiroExcedidoException("Sobregiro Excedido");
        }

        sobregiro.setMontoUtilizado(nuevoMontoProyectado );
        sobregiro.setDisponible(sobregiro.getMontoAprobado().subtract(sobregiro.getMontoUtilizado()));

        // 3. Guardamos y retornamos el objeto actualizado
        return sobregiroRepository.save(sobregiro);
    }
}