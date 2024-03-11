package com.server.quant_bot.comm.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.quant_bot.comm.exception.ResourceCommException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE+ ";charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        accessDeniedException.printStackTrace();
        response.getWriter().write(objectMapper.writeValueAsString("{ \"message\":" + accessDeniedException.getMessage() + "}" ));
    }
}
