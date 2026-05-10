package com.auth.web.util;

import com.auth.infrastructure.security.authentication.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {

    public static UUID getCurrentUserId() {

        Authentication authentication =
            SecurityContextHolder.getContext()
                .getAuthentication();

        CustomUserDetails user =
            (CustomUserDetails) authentication.getPrincipal();

        return UUID.fromString(user.getId());
    }
}