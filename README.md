# Diseñé y desarrollé el Core Banking para un sistema de gestión de cuentas corrientes, 
implementando una arquitectura de microservicios altamente escalable sobre Spring Cloud y Java 21. 
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

