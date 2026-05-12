package com.auth.infrastructure.security.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuditService {

    public void loginSuccess(
        String email,
        String ip
    ) {

        log.info(
            "LOGIN_SUCCESS email={} ip={}",
            email,
            ip
        );
    }

    public void loginFailure(
        String email,
        String ip
    ) {

        log.warn(
            "LOGIN_FAILURE email={} ip={}",
            email,
            ip
        );
    }

    public void logout(
        String userId
    ) {

        log.info(
            "LOGOUT userId={}",
            userId
        );
    }
}