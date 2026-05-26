package com.bci.tareas.security;

import jakarta.servlet.ServletException; // <--- Importante

import jakarta.servlet.FilterChain;
import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Collections;

@Component
//@Order(Ordered.HIGHEST_PRECEDENCE) // Añade esta línea (import org.springframework.core.annotation.Order e import org.springframework.core.Ordered)
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String userId = request.getHeader("X-User-Id");
        System.out.println("userId ::" +userId);

        final String authHeader = request.getHeader("Authorization");
        logger.info("Header recibido: " + authHeader); // Si esto sale null en la consola, Postman no está enviando el token
        String path = request.getServletPath();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
        String jwt = authHeader.substring(7);
        logger.info("jwt : " + jwt);
        String pwd = jwtService.extractUsername(jwt); // Debes tener este método en tu JwtService
        logger.info("pwd : " + pwd); // Si esto sale null en la consola, Postman no está enviando el token

            if (pwd != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                logger.info("entro 1 : ");

                if (jwtService.isTokenValid(jwt)) {
                    logger.info("entro 2 : ");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            pwd, null, Collections.emptyList());
                    logger.info("authToken : " + authToken.isAuthenticated());
                    // 6. Finalmente, guardamos la autenticación exitosa
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("Autenticación exitosa en el contexto para: " + pwd);
                  }
            }
        }catch (Exception e) {
            logger.info("No se pudo establecer la autenticación: {}" +e.getMessage());
            // No lanzamos error para que el filtro siga, pero Spring dará 403 al final
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Esto evita que el filtro se ejecute para rutas de error de Spring,
        // lo cual a veces causa que el contexto se limpie y de 403.
        return request.getServletPath().equals("/error");
    }
}