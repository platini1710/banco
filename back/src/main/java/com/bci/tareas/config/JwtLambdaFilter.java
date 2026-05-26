package com.bci.tareas.config;

import com.bci.tareas.security.LambdaAuthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Import agregado
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Import agregado
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtLambdaFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtLambdaFilter.class);

    private final LambdaAuthValidator authValidator;

    // Inyección por constructor (Spring pasa el Bean automáticamente)
    public JwtLambdaFilter(LambdaAuthValidator authValidator) {
        this.authValidator = authValidator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        logger.info("authHeader ::: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // ¡Aquí ejecutamos la llamada hacia AWS!
            boolean isTokenValid = authValidator.validarTokenConLambda(token);
            logger.info("isTokenValid ::: {}", isTokenValid);

            if (isTokenValid) {
                // 1. Creamos el objeto de autenticación para Spring Security.
                // Usamos "usuario_autenticado" como nombre temporal, null para la contraseña
                // y una lista vacía para los roles (Collections.emptyList()).
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        "usuario_autenticado",
                        null,
                        java.util.Collections.emptyList()
                );

                // 2. Le agregamos los detalles web de la petición actual (como la IP o sesión)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 3. Registramos oficialmente al usuario en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info("Token validado por Lambda. Petición autorizada en el contexto de Spring Security.");

            } else {
                // Token inválido o Lambda falló: Bloqueamos la petición
                logger.warn("Bloqueando petición: Token inválido o expirado");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token invalido o expirado");

                return; // Cortamos el flujo aquí, no llega al controlador
            }
        }

        // Si la petición no traía token (ej. una ruta permitida como el /login),
        // o si el token fue válido, dejamos que continúe su camino:
        filterChain.doFilter(request, response);
    }
}