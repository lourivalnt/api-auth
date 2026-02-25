package com.auth.infrastructure.web.mapper;

import com.auth.core.domain.model.Session;
import com.auth.infrastructure.web.dto.response.SessionResponse;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    SessionResponse toResponse(Session session);
}
