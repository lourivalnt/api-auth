package com.auth.service;

import com.auth.dto.request.RegisterRequest;
import com.auth.dto.response.UserResponseDTO;
public interface UserService {  

    UserResponseDTO create(RegisterRequest request);

    UserResponseDTO findByEmail(String email);

    UserResponseDTO getCurrentUser();
}
