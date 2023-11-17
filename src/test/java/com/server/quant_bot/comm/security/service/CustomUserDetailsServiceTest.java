package com.server.quant_bot.comm.security.service;

import com.server.quant_bot.comm.security.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class CustomUserDetailsServiceTest {


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("패스워드를 인코딩해야한다")
    void encodePasswordTest() {
        //given
        String userId = "userId";
        String password = "password";

        //when
        String encode = passwordEncoder.encode(password);
        boolean encodeRslt = passwordEncoder.matches(password, encode);

        //then
        log.info("::::::: this is password encoded::::::{}",encode);
        Assertions.assertThat(encodeRslt).isEqualTo(true);

    }

    @Test
    @DisplayName("시큐리티를 이용한 로그인에 성공해야 한다.")
    void loadUserByUsernameTest() {
        //given
        String userId = "userId";
        String password = "password";

        //when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
        String username = userDetails.getUsername();
        boolean isPwdEquals = passwordEncoder.matches(password, userDetails.getPassword());

        //log
        log.info("::::: user id = {}  :::::: userDetails.username = {}",userId,username);
        log.info("::::: user password = {}  :::::: userDetails.petPassword = {}",password,userDetails.getPassword());

        //then
        Assertions.assertThat(username).isEqualTo(userId);
        Assertions.assertThat(isPwdEquals).isTrue();


    }
}