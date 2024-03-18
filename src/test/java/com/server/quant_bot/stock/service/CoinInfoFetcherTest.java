package com.server.quant_bot.stock.service;

import com.server.quant_bot.stock.dto.CoinAllInfoDto;
import com.server.quant_bot.stock.dto.CoinCandleDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional(rollbackFor = Exception.class)
class CoinInfoFetcherTest {

    @Autowired
    public CoinInfoFetcher coinInfoFetcher;
    
    @Test
    @DisplayName("비트코인 전체정보 가져와야 한다")
    void getAllTest() {
        //given
        String bitCoin = "BTC";

        //when
        CoinAllInfoDto coinDto = coinInfoFetcher.getAll();
    
        //then
        Assertions.assertThat(coinDto.getCoinDetails().get(bitCoin)).isNotNull();
    
    }
    
    @Test
    @DisplayName("비트코인 시계열 데이터 전체를 가져와야한다")
    void getByTimeSeriesTest() {
        //given
        String bitCoin = "비트코인";
                
        //when
        CoinCandleDto byTimeSeries = coinInfoFetcher.getByTimeSeries(bitCoin);

        //then
        Assertions.assertThat( byTimeSeries ).isNotNull();
    
    }

}