package com.auth.mapper;

import com.auth.dto.response.UserResponseDTO;
import com.auth.entity.User;

public class UserMapper {

    private UserMapper() {}

    public static UserResponseDTO toResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .active(user.isActive())
                .build();
    }
}
