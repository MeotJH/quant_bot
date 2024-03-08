package com.server.quant_bot.comm.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class OauthKakaoLoginServiceTest {
    
    
    @Autowired
    public OauthKakaoLoginService oauthKakaoLoginService;

    @Test
    @DisplayName("카카오 인증로그인 가져온다")
    void doOauth() {
        //given

        //when
        ResponseEntity authorize = oauthKakaoLoginService.getAuthorize();
    
        //then
        log.info("::::{}::::", authorize.getBody());
        Assertions.assertThat(authorize).isNotNull();
    
    }
}