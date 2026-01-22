package com.auth.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.auth.dto.request.RegisterRequest;
import com.auth.dto.response.UserResponseDTO;
import com.auth.entity.Role;
import com.auth.entity.User;
import com.auth.exception.UserAlreadyExistsException;
import com.auth.mapper.UserMapper;
import com.auth.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    private RegisterRequest request() {
        return new RegisterRequest(
            "Paulo",
            "paulo@email.com",
            "123456"
        );
    }

    private User userEntity() {
        User u = new User();
        u.setId(1L);
        u.setName("Paulo");
        u.setEmail("paulo@email.com");
        u.setPassword("encoded");
        u.setRole(Role.ROLE_USER);
        u.setActive(true);
        return u;
    }

    private UserResponseDTO response() {
        return new UserResponseDTO(
            1L,
            "Paulo",
            "paulo@email.com",
            "ROLE_USER",
            true
        );
    }
    

    // ---------------------------------------------------
    // 🧪 1. Cenário de SUCESSO
    // ---------------------------------------------------
    @Test
    void testCreate_deveCriarUsuarioComSucesso() {
        
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(userEntity());

        Long result = userServiceImpl.create(request());

        assertNotNull(result);
        assertEquals(1L, result);
    }

    // ---------------------------------------------------
    // 🧪 2. Email duplicado → Exception
    // ---------------------------------------------------
    @Test
    void testCreate_deveLancarExcecaoQuandoEmailJaExistir() {
        when(userRepository.existsByEmail(any())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, 
            () -> userServiceImpl.create(request())
        );
    }

    
}