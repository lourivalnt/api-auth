package com.auth.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.dto.request.LoginRequest;
import com.auth.dto.request.RegisterRequest;
import com.auth.dto.response.AuthResponse;
import com.auth.entity.Role;
import com.auth.entity.User;
import com.auth.repository.UserRepository;
import com.auth.security.JwtTokenProvider;
import com.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Override
    public void register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);
        user.setActive(true);

        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.email(), 
                request.password())
        );

        String token = jwtTokenProvider.generateToken(authentication);

        return new AuthResponse(token, "Bearer");
    }

}
