package com.server.quant_bot.comm.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CorsOriginLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String origin = request.getHeader("Origin");
        // 원하는 로직으로 origin을 확인하고 로그로 출력
        if (origin != null && origin.equals("http://allowed-origin.com")) {
            log.info("Request from allowed origin:::::::::::::: {}", origin);
        } else {
            log.info("Request from disallowed origin:::::::::::::: {}", origin);
        }
        filterChain.doFilter(request, response);
    }
}