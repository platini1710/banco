package com.bci.tareas.security;

import com.bci.tareas.config.JwtLambdaFilter;
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
    private final JwtLambdaFilter jwtLambdaFilter;
    public SecurityConfig(JwtLambdaFilter jwtLambdaFilter) {
        this.jwtLambdaFilter = jwtLambdaFilter;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Esta pieza permite que Spring Security procese las contraseñas encriptadas
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtLambdaFilter jwtLambdaFilter) throws Exception {
        logger.info("Entró a securityFilterChain");

        return http
                // Desactivamos CSRF porque usaremos tokens (Stateless)
                .csrf(csrf -> csrf.disable())

                // Le decimos a Spring que no guarde sesiones en memoria
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // 1. Permitir el registro (Sign-up)
                        .requestMatchers(HttpMethod.POST, "/registro/usuario/**").permitAll()

                        // Permitir peticiones Preflight (CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Permitir endpoints públicos
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/canciones/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // 2. Permitir el Login
                        .requestMatchers(HttpMethod.POST, "/consulta/usuario/login/**").permitAll()

                        // 3. Todo lo demás bloqueado y requiere token
                        .anyRequest().authenticated()
                )
                // 4. REGISTRAMOS EL FILTRO (Ojo: ¡Sin punto y coma al final de esta línea!)
                .addFilterBefore(jwtLambdaFilter, UsernamePasswordAuthenticationFilter.class)


                // 5. Construimos la cadena final
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