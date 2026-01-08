package com.auth.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth.entity.RefreshToken;
import com.auth.entity.User;
import com.auth.exception.RefreshTokenException;
import com.auth.repository.RefreshTokenRepository;
import com.auth.repository.UserRepository;
import com.auth.service.RefreshTokenService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    private final RefreshTokenRepository repository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RefreshToken create(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        repository.deleteByUser(user); // ✅ agora seguro

        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plusSeconds(refreshTokenExpiration))
                .revoked(false)
                .build();

        return repository.save(token);
    }

    @Override
    public RefreshToken verify(String token) {
        // 🔎 só leitura → NÃO precisa de @Transactional
        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Refresh token inválido"));

        if (refreshToken.isRevoked()) {
            throw new RefreshTokenException("Refresh token revogado");
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RefreshTokenException("Refresh token expirado");
        }

        return refreshToken;
    }

    @Override
    @Transactional
    public RefreshToken rotate(RefreshToken oldToken) {

        oldToken.setRevoked(true);
        repository.save(oldToken); // ✅ mesma transação

        RefreshToken newToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(oldToken.getUser())
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .revoked(false)
                .build();

        return repository.save(newToken);
    }

    @Override
    @Transactional
    public void revoke(String token) {

        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Refresh token inválido ou inexistente"));

        refreshToken.setRevoked(true);
        repository.save(refreshToken);
    }
}
