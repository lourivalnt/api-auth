package com.auth.infrastructure.security.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RateLimitFilter
    extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (!path.startsWith("/auth/login")) {

            filterChain.doFilter(request, response);
            return;
        }

        String ip = extractClientIp(request);

        boolean allowed =
            rateLimitService.isAllowed(ip);

        if (!allowed) {

            response.setStatus(429);

            response.setContentType("application/json");

            response.getWriter().write("""
                {
                  "error": "Too many requests"
                }
            """);

            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractClientIp(
        HttpServletRequest request
    ) {

        String forwarded =
            request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {

            return forwarded.split(",")[0];
        }

        return request.getRemoteAddr();
    }
}