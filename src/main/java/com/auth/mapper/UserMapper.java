package com.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.auth.dto.response.UserResponseDTO;
import com.auth.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(user.getRole().name())")
    UserResponseDTO toResponse(User user);
}
