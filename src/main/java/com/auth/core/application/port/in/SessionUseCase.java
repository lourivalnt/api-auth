package com.auth.core.application.port.in;

import com.auth.web.dto.response.SessionResponse;

import java.util.List;

public interface SessionUseCase {

    List<SessionResponse> listSessions();
}