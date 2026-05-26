package com.bci.tareas.security;
// IMPORTANTE: Deja la línea "package com.tu_proyecto..." que tu IDE ponga aquí arriba

import com.fasterxml.jackson.databind.JsonNode;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class LambdaAuthValidator {

    private static final Logger logger = LoggerFactory.getLogger(LambdaAuthValidator.class);
    private final LambdaClient lambdaClient;
    private final ObjectMapper objectMapper;

    public LambdaAuthValidator(LambdaClient lambdaClient, ObjectMapper objectMapper) {
        this.lambdaClient = lambdaClient;
        this.objectMapper = objectMapper;
    }

    public boolean validarTokenConLambda(String token) {
        try {
            // 1. Armar el JSON (Payload) para Lambda
            Map<String, String> payloadMap = new HashMap<>();
            logger.info("token : {}", token);
            payloadMap.put("token", token);
            String payloadJson = objectMapper.writeValueAsString(payloadMap);

            SdkBytes payload = SdkBytes.fromUtf8String(payloadJson);

            // 2. Construir la petición
            InvokeRequest request = InvokeRequest.builder()
                    // Reemplaza esto con el nombre REAL de tu función Lambda en AWS
                    .functionName("autorizador-limpio")
                    .payload(payload)
                    .build();

            logger.info("Enviando token a AWS Lambda para validación...");

            // 3. Invocar Lambda
            InvokeResponse response = lambdaClient.invoke(request);

            // 4. Leer y mapear la respuesta
            String respuestaJson = response.payload().asUtf8String();
            logger.info("Respuesta de Lambda: {}", respuestaJson);
            JsonNode rootNode = objectMapper.readTree(respuestaJson);
            String effect = rootNode.path("policyDocument")
                    .path("Statement")
                    .get(0)
                    .path("Effect")
                    .asText();
            // 3. Comparamos si el efecto es exactamente "Allow"
            if ("Allow".equalsIgnoreCase(effect)) {
                logger.info("Lambda autorizó el token exitosamente (Effect: Allow).");
                return true;
            } else {
                logger.warn("Lambda denegó el token (Effect: {}).", effect);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error crítico comunicándose con AWS Lambda: ", e);
            return false; // Bloquear por defecto si hay error
        }
    }
}