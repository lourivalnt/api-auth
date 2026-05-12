package com.auth.infrastructure.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenEncoder {

    private final PasswordEncoder passwordEncoder;

    public String encode(String token) {

        return passwordEncoder.encode(token);
    }

    public boolean matches(
        String raw,
        String encoded
    ) {

        return passwordEncoder.matches(raw, encoded);
    }
}