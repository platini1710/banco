# Usamos una imagen base de Java compatible con tu proyecto (17 o 21) [cite: 7]
FROM amazoncorretto:21-alpine-jdk

RUN apk add --no-cache curl

# Definimos el directorio de trabajo
WORKDIR /app

# Copiamos el archivo JAR generado por Maven al contenedor [cite: 164]
COPY target/*.jar config_server.jar

# Ejecutamos la aplicación [cite: 308]
ENTRYPOINT ["java", "-jar", "config_server.jar"]