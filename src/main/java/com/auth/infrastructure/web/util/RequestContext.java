package com.auth.infrastructure.web.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.*;

public class RequestContext {

    public static String getIp() {

        HttpServletRequest request =
                ((ServletRequestAttributes)
                        RequestContextHolder.getRequestAttributes())
                        .getRequest();

        String xfHeader = request.getHeader("X-Forwarded-For");

        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }

        return request.getRemoteAddr();
    }
}