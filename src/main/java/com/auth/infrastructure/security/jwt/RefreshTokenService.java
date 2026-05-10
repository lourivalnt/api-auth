package com.auth.infrastructure.security.jwt;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class RefreshTokenService {

    private static final SecureRandom RANDOM =
        new SecureRandom();

    public String generate() {

        byte[] bytes = new byte[64];

        RANDOM.nextBytes(bytes);

        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes);
    }
}