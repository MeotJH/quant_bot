package com.server.quant_bot.korea.service;

import com.server.quant_bot.korea.dto.PublicDataStockDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;


@SpringBootTest
class PublicDataStockServiceTest {

    @Autowired
    private StockService stockService;


    @Test
    @DisplayName("삼성전자 주식을 가져와야 한다.")
    public void getServiceTest(){
        //given
        String SAMSUNG = "삼성전자";

        //when
        List<PublicDataStockDto> samsungStocks = stockService.get(SAMSUNG);
        PublicDataStockDto publicDataStockDto = samsungStocks.get(0);

        //then
        Assertions.assertThat(publicDataStockDto.getItmsNm()).isEqualTo(SAMSUNG);
    }

    @Test
    @DisplayName("삼성전자 주식을 가져와야 한다.")
    public void getByTikerLikeTest(){
        //given
        String SAMSUNG = "삼성";

        //when
        List<PublicDataStockDto> samsungStocks = stockService.get(SAMSUNG);
        PublicDataStockDto publicDataStockDto = samsungStocks.get(0);

        //then
        Assertions.assertThat(publicDataStockDto.getItmsNm()).isEqualTo(SAMSUNG);
    }

}