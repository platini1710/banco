package com.bci.tareas.services.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class PruebaDeEstresLocal {

    // Configuración de la prueba
    private static final int TOTAL_PETICIONES = 50000;
    private static final int TAMANO_POOL_HIKARI = 2;

    public static void main(String[] args) throws InterruptedException {

        System.out.println("🚀 Iniciando configuración de la Prueba de Estrés...");

        // 1. Configurar HikariCP por código (Sin Spring Boot)
        HikariConfig config = new HikariConfig();
       config.setJdbcUrl("jdbc:mysql://192.168.100.157:3306/mi_tienda_db");
        config.setUsername("mi_usuario");
        config.setPassword("secreto");
        config.setMaximumPoolSize(TAMANO_POOL_HIKARI);

        // Optimizaciones recomendadas por Hikari para alto rendimiento
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        // 2. Contadores seguros para hilos concurrentes
        AtomicInteger exitosas = new AtomicInteger(0);
        AtomicInteger fallidas = new AtomicInteger(0);

        // Este "cerrojo" nos avisa cuando los 50,000 hilos hayan terminado
        CountDownLatch cerrojoFinalizacion = new CountDownLatch(TOTAL_PETICIONES);

        // Iniciamos el Pool de Conexiones
        try (HikariDataSource dataSource = new HikariDataSource(config);
             var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            System.out.println("🔥 Disparando " + TOTAL_PETICIONES + " Hilos Virtuales...");
            long tiempoInicio = System.currentTimeMillis();

            // 3. Lanzar la tormenta de conexiones
            for (int i = 0; i < TOTAL_PETICIONES; i++) {
                executor.submit(() -> {
                    // El hilo intenta obtener una conexión del Pool
                    try (Connection conn = dataSource.getConnection();
                         PreparedStatement stmt = conn.prepareStatement("SELECT 1")) { // Query muy rápida

                        // Simulamos ejecución
                        stmt.executeQuery();
                        exitosas.incrementAndGet();

                    } catch (Exception e) {
                        fallidas.incrementAndGet();
                    } finally {
                        // Avisamos que este hilo ya terminó su trabajo
                        cerrojoFinalizacion.countDown();
                    }
                });
            }

            // 4. El hilo principal (main) se pausa aquí hasta que los 50,000 terminen
            cerrojoFinalizacion.await();

            long tiempoFin = System.currentTimeMillis();
            long tiempoTotal = tiempoFin - tiempoInicio;
            long transaccionesPorSegundo = (TOTAL_PETICIONES / tiempoTotal) * 1000;

            // 5. Reporte de Resultados
            System.out.println("\n📊 --- RESULTADOS DE LA PRUEBA ---");
            System.out.println("Tiempo Total     : " + tiempoTotal + " ms");
            System.out.println("Éxitos           : " + exitosas.get());
            System.out.println("Fallos           : " + fallidas.get());
            System.out.println("TPS (Rendimiento): " + transaccionesPorSegundo + " transacciones/segundo");
        }
    }
}