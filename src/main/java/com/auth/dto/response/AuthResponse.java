package com.auth.dto.response;

public record AuthResponse(
    String token,
    String tokenType
) {} 
