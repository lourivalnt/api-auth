package com.auth.dto.request;

import com.auth.validation.annotation.StrongPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3,max = 100, message = "Nome deve ter entre 3 e 100 caracteres") 
    String name,

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório") 
    @Size(max = 150, message = "Email deve ter no máximo 150 caracteres")
    String email,
    
    @NotBlank(message = "Senha é obrigatória") 
    @Size(min = 8, max = 32, message = "Senha deve ter no mínimo 8 caracteres")
    // @StrongPassword
    String password
) {} 
