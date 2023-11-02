package com.server.quant_bot.crawling.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NewsCrawlingTest {

    @Autowired
    Crawling crawling;

    @Test
    @DisplayName("크롤링한 List안 Map이 비어있지 않고 href인 key가 있다.")
    void get() {
        //given
        //when
        List<Map> maps = crawling.get();
        //then
        Assertions.assertFalse(maps.isEmpty());
        Assertions.assertTrue(maps.get(0).containsKey("href"));
    }
}