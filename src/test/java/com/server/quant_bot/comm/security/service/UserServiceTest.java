package com.server.quant_bot.comm.security.service;

import com.server.quant_bot.comm.security.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional(rollbackFor = Exception.class)
class UserServiceTest {

    @Autowired
    private UserService userService;


    @Test
    @DisplayName("유저 1명 넣는 테스트 한다.")
    void initUserTest() {
        //given
        String userId = "userId";

        // when
        UserEntity userEntity = userService.initUser();
        String savedUserId = userEntity.getUserId();

        //log
        log.info(":::::: userID = {} ::::::",userId);
        log.info(":::::: savedUserId = {} ::::::",savedUserId);

        //then
        Assertions.assertThat(savedUserId).isEqualTo(userId);

    }
}