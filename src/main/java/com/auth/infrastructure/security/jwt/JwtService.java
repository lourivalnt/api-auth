package com.auth.infrastructure.security.jwt;

import com.auth.configuration.security.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generateAccessToken(
            UUID userId,
            String email,
            UUID sessionId) {

        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .subject(userId.toString())

                .claim("email", email)
                .claim("sessionId", sessionId)

                .issuedAt(now)
                .expiration(expiration)

                .signWith(getKey())
                .compact();
    }

    public Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isValid(String token) {

        try {
            extractClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private SecretKey getKey() {

        return Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes());
    }

    public String generateRefreshToken() {

        return UUID.randomUUID() + "." + UUID.randomUUID();
    }

    public String generateRefreshAccessToken(
            UUID userId,
            String email,
            UUID sessionId) {

        return generateAccessToken(
                userId,
                email,
                sessionId);
    }
}