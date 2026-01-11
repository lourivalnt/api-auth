package com.auth.service;

import com.auth.dto.response.UserResponse;
public interface UserService {

    UserResponse getCurrentUser();

    UserResponse findByEmail(String email);
}
