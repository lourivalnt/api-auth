package com.auth.core.application.service.impl;

import com.auth.core.application.port.in.LoginUseCase;
import com.auth.core.application.port.in.RefreshTokenUseCase;
import com.auth.core.application.port.in.RegisterUseCase;
import com.auth.core.application.port.out.UserRepositoryPort;
import com.auth.core.domain.model.User;
import com.auth.infrastructure.cache.repository.SessionCacheService;
import com.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.auth.infrastructure.persistence.entity.SessionEntity;
import com.auth.infrastructure.persistence.repository.RefreshTokenJpaRepository;
import com.auth.infrastructure.persistence.repository.SessionJpaRepository;
import com.auth.infrastructure.security.jwt.JwtService;
import com.auth.infrastructure.security.jwt.RefreshTokenService;
import com.auth.web.dto.request.LoginRequest;
import com.auth.web.dto.request.RefreshTokenRequest;
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
        implements RegisterUseCase, LoginUseCase, RefreshTokenUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final SessionJpaRepository sessionRepository;
    private final RefreshTokenJpaRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final SessionCacheService sessionCacheService;

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
                        passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        UUID sessionId = UUID.randomUUID();

        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getEmail(),
                sessionId);

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
                        request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SessionEntity session = sessionRepository.save(
                SessionEntity.builder()
                        .userId(user.getId())
                        .device("UNKNOWN")
                        .ipAddress("UNKNOWN")
                        .createdAt(LocalDateTime.now())
                        .lastSeenAt(LocalDateTime.now())
                        .revoked(false)
                        .build());

        sessionCacheService.cacheSession(
                session.getId(),
                user.getId());

        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getEmail(),
                session.getId());

        String refreshToken = refreshTokenService.generate();

        refreshTokenRepository.save(
                RefreshTokenEntity.builder()
                        .sessionId(session.getId())
                        .token(refreshToken)
                        .expiresAt(
                                LocalDateTime.now().plusDays(7))
                        .createdAt(LocalDateTime.now())
                        .revoked(false)
                        .build());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(900L)
                .build();
    }

    public AuthResponse refreshToken(
            RefreshTokenRequest request) {

        RefreshTokenEntity storedToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (storedToken.isRevoked()) {

            throw new RuntimeException(
                    "Refresh token reuse detected");
        }

        if (storedToken.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "Refresh token expired");
        }

        SessionEntity session = sessionRepository.findById(
                storedToken.getSessionId()).orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.isRevoked()) {

            throw new RuntimeException(
                    "Session revoked");
        }

        User user = userRepository.findById(
                session.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        // ROTATION
        storedToken.setRevoked(true);

        refreshTokenRepository.save(storedToken);

        String newRefreshToken = refreshTokenService.generate();

        RefreshTokenEntity newToken = refreshTokenRepository.save(
                RefreshTokenEntity.builder()
                        .sessionId(session.getId())
                        .token(newRefreshToken)
                        .expiresAt(
                                LocalDateTime.now().plusDays(7))
                        .createdAt(LocalDateTime.now())
                        .revoked(false)
                        .build());

        String accessToken = jwtService.generateRefreshAccessToken(
                user.getId(),
                user.getEmail(),
                session.getId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newToken.getToken())
                .tokenType("Bearer")
                .expiresIn(900L)
                .build();
    }
}