package com.auth.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.auth.core.domain.model.RefreshToken;
import com.auth.infrastructure.persistence.entity.RefreshTokenEntity;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {

    RefreshTokenEntity toEntity(RefreshToken domain);

    RefreshToken toDomain(RefreshTokenEntity entity);
}
