package com.example.apiGateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

@Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable()) // Desactiva también CORS por si acaso
            .authorizeExchange(exchanges -> exchanges
                .anyExchange().permitAll() // PERMITIR TODO por ahora para probar
            )
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())
            .build();
    }

}
