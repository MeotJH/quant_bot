package com.server.quant_bot.comm.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.quant_bot.comm.security.enums.Auth;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE+ ";charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString("{ \"message\":\"" + authException.getMessage() + "\"}" ));
        deleteAuthorization(response);

        //TODO 에러 페이지 만들기
    }

    private static void deleteAuthorization(HttpServletResponse response) {
        Cookie authorization = new Cookie(Auth.AUTHORIZATION.getValue(), "");
        authorization.setMaxAge(0);
        authorization.setPath("/");
        response.addCookie(authorization);
    }
}
