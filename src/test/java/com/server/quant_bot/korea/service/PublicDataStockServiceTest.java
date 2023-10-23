package com.server.quant_bot.korea.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PublicDataStockServiceTest {

    @Autowired
    private StockService stockService;


    @Test
    public void getTest(){
        //given

        //when
        ResponseEntity<Map> response = stockService.get("%EC%82%BC%EC%84%B1%EC%A0%84%EC%9E%90");
        //then
        Assertions.assertThat(response).isNotEqualTo(null);
    }

}