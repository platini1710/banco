package com.bci.tareas.services.impl;

import com.bci.tareas.services.ProcesoMoraDiariaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ProcesoMoraDiariaServiceImpl  implements ProcesoMoraDiariaService {

    // Inyectarías tu Repositorio aquí
    // @Autowired
    // private AvanceCuotasRepository cuotasRepository;

    /*
     * Formato CRON: "Segundo Minuto Hora Día_Mes Mes Día_Semana"
     * "0 59 23 * * ?" significa: Todos los días a las 23:59:00
     */
    @Scheduled(cron = "0 12 21 * * ?")
    public void ejecutarCalculoDeMora() {
        System.out.println("Iniciando proceso de mora diaria a las: " + LocalDateTime.now());

        // 1. Buscar todas las cuotas vencidas en la BD
        // List<AvanceCuota> cuotasVencidas = cuotasRepository.findCuotasVencidas(LocalDate.now());

        // 2. Recorrer cada cuota con un ForEach
        // for (AvanceCuota cuota : cuotasVencidas) {
        //     // Calcular el aumento
        //     BigDecimal interesDiario = cuota.getMontoCuota().multiply(TASA_MORA_DIARIA);
        //     cuota.setMontoMoraAcumulado(cuota.getMontoMoraAcumulado().add(interesDiario));
        //     cuotasRepository.save(cuota);
        // }

        System.out.println("Proceso de mora finalizado con éxito.");
    }
}
