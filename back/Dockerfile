# 1. Usar una imagen base ligera de Java 21 (Amazon Corretto)
FROM amazoncorretto:21-alpine-jdk

# 2. Copiar el archivo .jar generado por Gradle a la imagen
COPY build/libs/*.jar app.jar

# 3. Exponer el puerto (Ajusta este número al puerto real de tu microservicio)
EXPOSE 8080 

# 4. El comando que ejecutará Docker al encender el contenedor
ENTRYPOINT ["java", "-jar", "/app.jar"]