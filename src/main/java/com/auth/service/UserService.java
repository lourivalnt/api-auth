package com.auth.service;

import com.auth.dto.response.UserResponseDTO;
public interface UserService {

    UserResponseDTO getCurrentUser();

    UserResponseDTO findByEmail(String email);
}
