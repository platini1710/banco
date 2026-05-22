package com.bci.tareas.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.util.function.Function;
import java.util.Date;

@Service
public class JwtService {
    // LLAVE_SECRETA debe tener al menos 32 caracteres
    private static final String SECRET = "MiLlaveSuperSecretaDeBciParaSeguridad2026";

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    // 1. Extraer el nombre de usuario (el "Subject" del token)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 2. Método genérico para extraer cualquier dato (Claim) del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 3. Validar si el token es correcto y no ha expirado
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token); // Si logra parsearlo sin error, es válido
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // 4. Este es el método que realmente abre el token usando la llave secreta
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
