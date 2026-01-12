package com.wallet.mini_wallet_service.service.security;

import com.wallet.mini_wallet_service.service.exception.CustomErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final tools.jackson.databind.ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        CustomErrorResponse error = new CustomErrorResponse(
                "UNAUTHORIZED",
                "Authentication token is missing or invalid"
        );

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}

