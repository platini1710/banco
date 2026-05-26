package com.bci.tareas.config;
import com.bci.tareas.security.LambdaAuthValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AwsConfig {

    // 1. Le enseñamos a Spring cómo conectarse a AWS
    @Bean
    public LambdaClient lambdaClient() {
        return LambdaClient.builder()
                .region(Region.US_EAST_2) // Asegúrate de poner la región donde está tu Lambda
                // Si la MV2 tiene su Rol IAM configurado, no necesitas poner credenciales aquí,
                // el SDK las tomará automáticamente de la instancia.
                .build();
    }

    // 2. Registramos nuestra clase validadora en Spring, inyectándole el LambdaClient y el ObjectMapper
    @Bean
    public LambdaAuthValidator lambdaAuthValidator(LambdaClient lambdaClient, ObjectMapper objectMapper) {
        return new LambdaAuthValidator(lambdaClient, objectMapper);
    }
}