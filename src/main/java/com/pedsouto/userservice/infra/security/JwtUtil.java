package com.pedsouto.userservice.infra.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secretString,
            @Value("${jwt.expiration}") long expirationTime
    ) {
        if (secretString == null || secretString.isBlank()) {
            throw new IllegalArgumentException("JWT secret must not be empty");
        }
        if (expirationTime <= 0) {
            throw new IllegalArgumentException("JWT expiration must be greater than zero");
        }

        this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractClaims(token).getExpiration();
        return expiration == null || expiration.before(new Date());
    }

    public boolean validateToken(String token, String username) {
        try {
            Claims claims = extractClaims(token);
            Date expiration = claims.getExpiration();

            return claims.getSubject() != null
                    && claims.getSubject().equals(username)
                    && expiration != null
                    && expiration.after(new Date());
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
}
