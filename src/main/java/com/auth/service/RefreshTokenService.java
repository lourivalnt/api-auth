package com.auth.service;

import com.auth.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken create(String email);

    RefreshToken verify(String token);

    RefreshToken rotate(RefreshToken token);

    void revoke(String token);
}
