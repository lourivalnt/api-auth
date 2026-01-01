package com.auth.service.impl;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.dto.request.LoginRequest;
import com.auth.dto.request.RefreshTokenRequest;
import com.auth.dto.request.RegisterRequest;
import com.auth.dto.response.AuthResponse;
import com.auth.entity.RefreshToken;
import com.auth.entity.Role;
import com.auth.entity.User;
import com.auth.repository.UserRepository;
import com.auth.security.JwtTokenProvider;
import com.auth.service.AuthService;
import com.auth.service.RefreshTokenService;
import com.auth.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);
        user.setActive(true);

        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()));

        String accessToken = jwtTokenProvider.generateToken(authentication);

        RefreshToken refreshToken = refreshTokenService.create(authentication.getName());

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer");
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        // 1️⃣ Verifica se o refresh token é válido
        RefreshToken token = refreshTokenService.verify(refreshToken);

        User user = token.getUser();

        // 2️⃣ Cria Authentication manualmente
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                List.of(new SimpleGrantedAuthority(
                        user.getRole().name())));

        // 3️⃣ Gera novo access token
        String newAccessToken = jwtTokenProvider.generateToken(authentication);

        // 4️⃣ (opcional, mas recomendado) Rotaciona refresh token
        RefreshToken newRefreshToken = refreshTokenService.rotate(token);

        return new AuthResponse(
                newAccessToken,
                newRefreshToken.getToken(),
                "Bearer");
    }

    @Override
    public void logout() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logout'");
    }

}
