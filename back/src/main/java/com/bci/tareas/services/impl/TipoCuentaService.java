package com.bci.tareas.services.impl;

import com.bci.tareas.model.TipoCuenta;
import com.bci.tareas.repositorio.TipoCuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoCuentaService {

    @Autowired
    private TipoCuentaRepository repository;

    public void probarBaseDeDatos() {
        // 1. Crear y guardar un nuevo registro (INSERT)
        TipoCuenta cuentaCorriente = new TipoCuenta("CUENTA_CORRIENTE", "Cuenta de liquidez inmediata");
        repository.save(cuentaCorriente);

        // 2. Buscar todos los registros (SELECT * FROM tipo_cuenta)
        System.out.println("Total de tipos: " + repository.findAll().size());

        // 3. Buscar usando tu llave única (UNIQUE KEY `nombre`)
        repository.findByNombre("CUENTA_CORRIENTE").ifPresent(cuenta -> {
            System.out.println("Encontré la cuenta con ID: " + cuenta.getIdTipoCuenta());
        });
    }
}