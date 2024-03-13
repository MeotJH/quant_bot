package com.server.quant_bot.stock.service;

import com.server.quant_bot.stock.dto.PublicDataStockDto;
import com.server.quant_bot.stock.entity.Stock;
import com.server.quant_bot.stock.mapping.StockMapping;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


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
    @DisplayName("테이블에 CSV데이터를 넣어야한다.")
    @Transactional(rollbackFor = Exception.class)
    void CSVToDB() {
        //given
        //when
        List<Stock> stocks = stockService.FetchToDB();
        Stock stock = stocks.get(0);

        //then
        Assertions.assertThat(stock.getStockCode()).isNotNull();
    }

    @Test
    @DisplayName("주식명으로 이름이 like검색인 애들 다 가져와야 한다.")
    void getStocksByStockLike() {
        //gieven
        String keyword = "삼성";

        //when
        List<StockMapping> stocksByStockLike = stockService.getStocksByStockLike("%"+keyword+"%");

        //then
        Assertions.assertThat( stocksByStockLike.get(0).getStockName().contains(keyword) ).isEqualTo(true);

    }
}