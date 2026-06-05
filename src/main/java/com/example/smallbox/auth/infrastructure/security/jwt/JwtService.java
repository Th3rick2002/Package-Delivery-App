package com.example.smallbox.auth.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationTime;
    private final long refreshTokenTime;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-time-ms}") long expirationTime,
            @Value("${jwt.expiration-time-refresh-ms}") long refreshTokenTime
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
        this.refreshTokenTime = refreshTokenTime;
    }

    public String generateToken(UUID userId, String email, String role, JWTType type) {
        long expiration = type == JWTType.ACCESS ? this.expirationTime : this.refreshTokenTime;

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("userId", userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String generateInternalToken(UUID userId, String email, Integer branchId, String role, JWTType type) {
        long expiration = type == JWTType.ACCESS ? this.expirationTime : this.refreshTokenTime;

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("branchId", branchId)
                .claim("userId", userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return getClaims(token).get("role", String.class);
    }

    public UUID getUserIdFromToken(String token) {
        return UUID.fromString(getClaims(token).get("userId", String.class));
    }

    public Integer getBranchIdFromToken(String token) {
        return getClaims(token).get("branchId", Integer.class);
    }

    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
