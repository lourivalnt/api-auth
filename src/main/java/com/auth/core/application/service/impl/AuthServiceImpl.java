package com.auth.core.application.service.impl;

import com.auth.core.application.port.in.LoginUseCase;
import com.auth.core.application.port.in.RegisterUseCase;
import com.auth.core.application.port.out.UserRepositoryPort;
import com.auth.core.domain.model.User;
import com.auth.infrastructure.security.jwt.JwtService;
import com.auth.web.dto.request.LoginRequest;
import com.auth.web.dto.request.RegisterRequest;
import com.auth.web.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl
    implements RegisterUseCase, LoginUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {

        userRepository.findByEmail(request.getEmail())
            .ifPresent(user -> {
                throw new RuntimeException("User already exists");
            });

        User user = User.builder()
            .id(UUID.randomUUID())
            .email(request.getEmail())
            .password(
                passwordEncoder.encode(request.getPassword())
            )
            .role("USER")
            .createdAt(LocalDateTime.now())
            .build();

        userRepository.save(user);

        UUID sessionId = UUID.randomUUID();

        String accessToken =
            jwtService.generateAccessToken(
                user.getId(),
                user.getEmail(),
                sessionId
            );

        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken("temporary-refresh-token")
            .tokenType("Bearer")
            .expiresIn(900L)
            .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                new RuntimeException("User not found")
            );

        UUID sessionId = UUID.randomUUID();

        String accessToken =
            jwtService.generateAccessToken(
                user.getId(),
                user.getEmail(),
                sessionId
            );

        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken("temporary-refresh-token")
            .tokenType("Bearer")
            .expiresIn(900L)
            .build();
    }
}