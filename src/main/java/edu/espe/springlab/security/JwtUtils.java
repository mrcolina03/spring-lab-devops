package edu.espe.springlab.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // Valores configurados desde el archivo application.yml
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    // Crea un nuevo token JWT con el usuario recibido
    public String generateToken(String username) {
        // Se genera la clave a partir del secret configurado
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        // Se construye y firma el token
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Comprueba si un token es válido
    public boolean validateToken(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        try {
            // Si no lanza excepción, el token es correcto
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            // Cualquier error implica token no válido
            return false;
        }
    }

    // Extrae el nombre de usuario almacenado dentro del token
    public String getUsernameFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
