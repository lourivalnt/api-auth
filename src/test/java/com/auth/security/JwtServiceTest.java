package com.auth.security;

import com.auth.configuration.security.JwtProperties;
import com.auth.infrastructure.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() {

        JwtProperties properties =
            new JwtProperties();

        properties.setSecret(
            "12345678901234567890123456789012"
        );

        properties.setExpiration(900000);

        jwtService = new JwtService(properties);
    }

    @Test
    void shouldGenerateAndValidateToken() {

        String token =
            jwtService.generateAccessToken(
                UUID.randomUUID(),
                "user@email.com",
                UUID.randomUUID()
            );

        assertNotNull(token);

        assertTrue(jwtService.isValid(token));
    }
}