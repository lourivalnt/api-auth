package com.auth.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.auth.core.domain.model.Session;
import com.auth.infrastructure.persistence.entity.SessionEntity;

@Mapper(componentModel = "spring")
public interface SessionEntityMapper {

    SessionEntity toEntity(Session domain);

    Session toDomain(SessionEntity entity);
}
