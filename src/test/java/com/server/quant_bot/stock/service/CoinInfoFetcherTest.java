package com.server.quant_bot.stock.service;

import com.server.quant_bot.stock.dto.CoinDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CoinInfoFetcherTest {

    @Autowired
    public CoinInfoFetcher coinInfoFetcher;
    
    @Test
    @DisplayName("비트코인 전체정보 가져와야 한다")
    void getAllTest() {
        //given
        String bitCoin = "BTC";

        //when
        CoinDto coinDto = coinInfoFetcher.getAll();
    
        //then
        Assertions.assertThat(coinDto.getCoinDetails().get(bitCoin)).isNotNull();
    
    }

}