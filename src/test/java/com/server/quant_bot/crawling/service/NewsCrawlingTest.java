package com.server.quant_bot.crawling.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NewsCrawlingTest {

    @Autowired
    Crawling crawling;

    @Test
    @DisplayName("뉴스 마크업을 리스트로 가져온다")
    void get() {
        //given
        //when
        int i = crawling.get();
        //then
        Assertions.assertNotEquals(i,0);
    }
}