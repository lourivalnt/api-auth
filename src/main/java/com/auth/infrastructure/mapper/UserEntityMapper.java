package com.auth.infrastructure.mapper;

import com.auth.core.domain.model.User;
import com.auth.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);
}
