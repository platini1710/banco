# 🏦 Sistema Bancario Moderno (Microservicios & Observabilidad)

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Containerized-blue.svg)](https://www.docker.com/)

## 📖 Descripción del Proyecto
Este proyecto simula la arquitectura backend de un sistema bancario moderno basado en **Microservicios**. Está diseñado para garantizar alta disponibilidad, separación de responsabilidades y un monitoreo avanzado del flujo de transacciones financieras.

---

## 🏛️ Arquitectura del Sistema
El sistema está compuesto por servicios independientes que se comunican de forma segura y estructurada:
* **API Gateway:** Punto de entrada único que enruta las peticiones de los clientes.
* **Service Registry (Eureka):** Permite el descubrimiento dinámico de instancias de microservicios en tiempo real.
* **Config Server:** Centraliza la configuración de los microservicios conectándose a un repositorio Git.
* **Base de Datos por Servicio:** Cada microservicio gestiona exclusivamente su propia fuente de datos para evitar acoplamientos y cuellos de botella.

---

## 📊 Observabilidad y Rastreo Distribuido
Para resolver el desafío de rastrear peticiones a través de múltiples servicios, el sistema implementa:
* **Micrometer Tracing & OpenTelemetry:** Inyección automática de `Trace IDs` y `Span IDs` globales en los logs y cabeceras HTTP.
* **Zipkin:** Visualización de gráficos temporales (Gantt charts) para auditar latencias y detectar cuellos de botella en tiempo real.

---

## 🛠️ Stack Tecnológico
* **Core:** Java 17, Spring Boot 3.x, Spring Cloud.
* **Monitoreo & Tracing:** Micrometer, OpenTelemetry, Zipkin, Spring Boot Actuator.
* **Infraestructura:** Docker y Docker Compose para despliegue de dependencias locales.
* **Gestión de Dependencias:** Maven.

---

## 🚀 Guía de Inicio Rápido (Quick Start)

Sigue estos pasos para clonar y ejecutar el proyecto en tu entorno local:

### 1. Clonar el Repositorio
```bash
git clone [https://github.com/platini1710/banco.git](https://github.com/platini1710/banco.git)
cd banco

# Diseñé y desarrollé el Core Banking para un sistema de gestión de cuentas corrientes, 
implementando una arquitectura de microservicios altamente escalable sobre Spring Cloud,hibernate 6,Spring Boot 3.2 y Java 21. 
Para cumplir con los exigentes Acuerdos de Nivel de Servicio (SLA) del sector financiero y 
soportar miles de transacciones por minuto, habilité el modelo de hilos virtuales de Project Loom (spring.threads.virtual.enabled=true),
 optimizando drásticamente el rendimiento en operaciones de entrada/salida (I/O).

# 

# La integridad de las transacciones concurrentes se garantizó mediante
 estrategias de bloqueo pesimista a nivel de base de datos (@Lock(LockModeType.PESSIMISTIC\_WRITE)), 
 eliminando el riesgo de condiciones de carrera en operaciones de saldo. Adicionalmente, 
 orquesté el procesamiento batch de rutinas financieras utilizando @EnableScheduling,
 y aseguré la alta disponibilidad del sistema distribuyendo el tráfico mediante balanceadores de carga en AWS. 
 En la capa de presentación, construí una interfaz reactiva y 
 de alto rendimiento utilizando las capacidades concurrentes de React 18, 
 asegurando un consumo eficiente y robusto de las APIs RESTful del backend.



pueden revisar en código back en la carpeta Backend y el Front en la carpeta fronReact

**AUTOR	   : 	AUGUSTO ESPINOZA N.**  
**PROYECTO :	CUENTA CORRIENTE BANCO**

