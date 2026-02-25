package com.auth.infrastructure.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.auth.core.domain.model.User;
import com.auth.infrastructure.persistence.entity.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);
}
