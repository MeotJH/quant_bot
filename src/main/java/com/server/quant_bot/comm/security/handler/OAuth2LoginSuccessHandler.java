package com.server.quant_bot.comm.security.handler;

import com.server.quant_bot.comm.security.dto.TokenInfo;
import com.server.quant_bot.comm.security.service.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 참고 코드
 * https://ksh-coding.tistory.com/66#2. OAuth DTO 클래스 - OAuthAttributes-1
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    //TODO 이거 환경변수에서 주입받아야 될것 같은데..
    private final static String SIGN_UP_PAGE = "/view/sign-up";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

            //1. 처음인지 아닌지 분기처리
            //1.1 처음이면 회원가입 추가정보? 아니면 그냥할지 정해서 보내기
            //1.2 처음아니면 로그인 석세스 시키기

            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
            setTokens(response,tokenInfo);
            response.sendRedirect(SIGN_UP_PAGE); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트
        } catch (Exception e) {
            throw e;
        }

    }

    private void setTokens(HttpServletResponse response, TokenInfo tokenInfo){

        //이부분 리팩토링
        Map<String,String> map = new HashMap<>();
        map.put(jwtTokenProvider.getRefreshTokenHeaderName(), tokenInfo.getRefreshToken());
        map.put(jwtTokenProvider.getAccessTokenHeaderName(),"Bearer%20"+tokenInfo.getAccessToken());

        for (String key :map.keySet()){
            setTokenToCookieHttpOnly(response, key, map);
        }
    }

    private void setTokenToCookieHttpOnly(HttpServletResponse response, String key, Map<String,String> tokenMap){
        Cookie cookie = new Cookie(key,tokenMap.get(key));

        boolean isHttpOnly = key.equals(jwtTokenProvider.getRefreshTokenHeaderName()) ? true : false;
        cookie.setHttpOnly(isHttpOnly);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
