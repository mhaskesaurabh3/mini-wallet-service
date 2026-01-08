package com.wallet.mini_wallet_service.service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private byte[] keyBytes;

    @PostConstruct
    private void init() {
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalStateException("Missing required property 'jwt.secret'. Set it in application.properties or via environment variable (e.g. JWT_SECRET).");
        }

        // Try decode as Base64, otherwise use raw bytes
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(secret);
        } catch (IllegalArgumentException e) {
            decoded = secret.getBytes(StandardCharsets.UTF_8);
        }

        if (decoded.length < 32) { // HS256 requires >= 256 bits (32 bytes) key material for secure use
            throw new IllegalStateException("Property 'jwt.secret' must be at least 32 bytes (use e.g. `openssl rand -base64 32`).");
        }

        this.keyBytes = decoded;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Long userId, String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
