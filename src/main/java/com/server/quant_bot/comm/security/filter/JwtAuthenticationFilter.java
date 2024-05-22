package com.server.quant_bot.comm.security.filter;

import com.server.quant_bot.comm.security.service.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 1. Request Header 에서 JWT 토큰 추출
        String token = resolveToken((HttpServletRequest) request);

        if( token == null || !jwtTokenProvider.validateToken(token)){
            chain.doFilter(request,response);
            return;
        }

        // 2. validateToken 으로 토큰 유효성 검사
        // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

        //TODO ExpiredJwtException try-catch만들어서 만료되었으면 프론트에서 알림주는 기능 만들기
    }

    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) throws UnsupportedEncodingException {

        Cookie cookie = new Cookie("Authorization", null);
        if( request.getCookies() != null){
            List<Cookie> authorization =
                    Arrays
                            .stream( request.getCookies() )
                            .filter( each -> each.getName().equals("Authorization") )
                            .collect( Collectors.toList() );

            cookie = authorization.size() == 1 ? authorization.get(0) : null;
        }

        String auth = request.getHeader("Authorization");
        if(cookie == null || auth == null){
            return null;
        }
        if(cookie.getValue() != null){
            auth = cookie.getValue();
        }

        //front 에서 던질때 encode한걸 던지고있다.
        String bearerToken = URLDecoder.decode(auth, "UTF-8");
        log.info("::::::cookie.Authorization::::::::{}",bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }

        //return null 대신 뭐 없나.
        return null;
    }
}
