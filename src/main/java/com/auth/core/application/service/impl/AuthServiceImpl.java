package com.auth.core.application.service.impl;

import com.auth.core.application.exception.InvalidCredentialsException;
import com.auth.core.application.exception.TooManyRequestsException;
import com.auth.core.application.port.in.AuthUseCase;
import com.auth.core.application.port.out.UserPersistencePort;
import com.auth.core.application.service.RefreshTokenService;
import com.auth.core.domain.model.Session;
import com.auth.infrastructure.security.JwtService;
import com.auth.infrastructure.security.PasswordEncoderAdapter;
import com.auth.infrastructure.security.ratelimit.RateLimiterService;
import com.auth.infrastructure.web.dto.response.AuthTokens;
import com.auth.infrastructure.web.util.RequestContext;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthUseCase {

    private final UserPersistencePort userRepository;
    private final PasswordEncoderAdapter encoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final SessionService sessionService;
    private final RateLimiterService rateLimiterService;

    @Override
    public AuthTokens login(String email, String password) {

        String ip = RequestContext.getIp(); // explico abaixo

        if (rateLimiterService.isBlocked(ip)) {
            throw new TooManyRequestsException();
        }

        try {

            var user = userRepository.findByEmail(email)
                    .orElseThrow(InvalidCredentialsException::new);

            if (!encoder.matches(password, user.getPassword())) {
                rateLimiterService.registerAttempt(ip);
                throw new InvalidCredentialsException();
            }

            /*
                ✅ Login bem-sucedido → reset contador
            */
            rateLimiterService.reset(ip);

            Session session = sessionService.create(user.getId(), "web", ip);

            var refreshToken =
                    refreshTokenService.create(user.getId(), session.getId());

            return new AuthTokens(
                    jwtService.generateToken(user.getId(), session.getId()),
                    refreshToken.getToken()
            );

        } catch (InvalidCredentialsException ex) {

            rateLimiterService.registerAttempt(ip);
            throw ex;
        }
    }

    @Override
    public String refresh(UUID refreshTokenId) {

        var stored = refreshTokenService.validate(refreshTokenId);

        return jwtService.generateToken(
                stored.getUserId(),
                stored.getSessionId());
    }

    @Override
    public void logoutAll(UUID userId) {
        sessionService.revokeAll(userId);
    }

    @Override
    public List<Session> listSessions(UUID userId) {
        return sessionService.findActiveByUser(userId);
    }

    @Override
    public void logoutSession(UUID userId, UUID sessionId) {
        /// Futuramente podemos validar se a sessão pertence ao usuário
        sessionService.revoke(sessionId);
    }

}
