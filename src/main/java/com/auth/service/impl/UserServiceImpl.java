package com.auth.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.auth.dto.response.UserResponseDTO;
import com.auth.entity.User;
import com.auth.mapper.UserMapper;
import com.auth.repository.UserRepository;
import com.auth.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder
            .getContext()
            .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> 
                new IllegalStateException("Usuário autenticado não encontrado"));

        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponseDTO findByEmail(String email) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> 
                new IllegalArgumentException("Usuário não encontrado"));
        
        return UserMapper.toResponse(user);
    }

}
