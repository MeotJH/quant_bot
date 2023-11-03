package com.server.quant_bot.korea.service;

import com.server.quant_bot.korea.MarketDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class NaverMarketServiceTest {

    @Autowired
    MarketService marketService;

    @Test
    @DisplayName("코스닥 및 코스피 정보와 사진 List 값 수가 같아야 한다.")
    void getData() {
        //given

        //when
        MarketDto dto = marketService.getData();
        List datas = dto.maps().get("data");
        List imgs = dto.maps().get("img");

        //then
        Assertions.assertThat(datas.size()).isEqualTo(imgs.size());
    }
}