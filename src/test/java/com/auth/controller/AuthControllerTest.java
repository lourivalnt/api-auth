package com.auth.controller;

import com.auth.core.application.port.in.LoginUseCase;
import com.auth.core.application.port.in.RegisterUseCase;
import com.auth.web.controller.AuthController;
import com.auth.web.dto.request.LoginRequest;
import com.auth.web.dto.response.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoginUseCase loginUseCase;

    @MockitoBean
    private RegisterUseCase registerUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldLoginSuccessfully() throws Exception {

        LoginRequest request = new LoginRequest();

        request.setEmail("user@email.com");
        request.setPassword("123456");

        Mockito.when(loginUseCase.login(Mockito.any()))
            .thenReturn(
                AuthResponse.builder()
                    .accessToken("jwt")
                    .refreshToken("refresh")
                    .tokenType("Bearer")
                    .expiresIn(900L)
                    .build()
            );

        mockMvc.perform(
                post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk());
    }
}