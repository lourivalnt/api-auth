package com.auth.core.application.port.out;

import java.util.Optional;
import java.util.UUID;

import com.auth.core.domain.model.RefreshToken;

public interface RefreshTokenPersistencePort {

    RefreshToken save(RefreshToken token);

    Optional<RefreshToken> findByToken(UUID token);

    void revoke(UUID token);

    void revokeAllByUser(UUID userId);

}
