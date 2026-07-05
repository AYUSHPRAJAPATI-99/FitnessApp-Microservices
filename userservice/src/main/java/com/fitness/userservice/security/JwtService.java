package com.fitness.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    // At least 32 characters
    @Value("${jwt.secret}")
    private String SECRET;
    private static final long EXPIRATION =
            1000 * 60 * 60;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(
                SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // Generate JWT
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()
                                        + EXPIRATION))
                .signWith(getKey())
                .compact();
    }

    // Extract Email
    public String extractEmail(String token) {
        return extractClaims(token)
                .getSubject();
    }

    // Extract All Claims
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Check Expiry
    public boolean isTokenExpired(String token) {
        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }
    // Validate Token
    public boolean validateToken(String token, String email) {
        return extractEmail(token)
                .equals(email)
                && !isTokenExpired(token);
    }
}