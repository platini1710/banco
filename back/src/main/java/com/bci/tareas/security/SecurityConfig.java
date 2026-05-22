package com.bci.tareas.security;

import com.bci.tareas.controllers.ControllerConsulta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Esta pieza permite que Spring Security procese las contraseñas encriptadas
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        logger.info("entro a securityFilterChain ");
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // 1. Permitir el registro (Sign-up)
                        .requestMatchers(HttpMethod.POST,"/registro/usuario/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/canciones/**").permitAll()
                        // 2. Permitir el Login (Asegúrate que el prefijo sea /consulta/usuario)
                        .requestMatchers(HttpMethod.POST, "/consulta/usuario/login/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        // 3. Todo lo demás bloqueado
                        .anyRequest().authenticated()
                )// ESTA LÍNEA REGISTRA EL FILTRO:

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Este método expone el AuthenticationManager configurado por Spring
        return config.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 1. Permitimos explícitamente a tu React (Vite)
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        // 2. Permitimos los métodos HTTP que vas a usar (incluyendo OPTIONS para el preflight)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 3. Permitimos los headers estándar
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        // 4. Si vas a usar cookies o tokens en el header, esto es necesario
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplicamos esta regla a todas las rutas de tu API
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}