package com.auth.infrastructure.persistence.mapper;

import com.auth.core.domain.model.User;
import com.auth.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(User user);

    User toDomain(UserEntity entity);
}