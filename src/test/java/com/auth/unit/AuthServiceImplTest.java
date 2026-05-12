package com.auth.unit;

import com.auth.core.application.port.out.UserRepositoryPort;
import com.auth.core.application.service.impl.AuthServiceImpl;
import com.auth.infrastructure.security.jwt.JwtService;
import com.auth.web.dto.request.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setup() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterUserSuccessfully() {

        RegisterRequest request =
            new RegisterRequest();

        request.setEmail("user@email.com");
        request.setPassword("123456");

        when(userRepository.findByEmail(any()))
            .thenReturn(java.util.Optional.empty());

        when(passwordEncoder.encode(any()))
            .thenReturn("hashed-password");

        assertDoesNotThrow(() ->
            authService.register(request)
        );

        verify(userRepository, times(1))
            .save(any());
    }
}