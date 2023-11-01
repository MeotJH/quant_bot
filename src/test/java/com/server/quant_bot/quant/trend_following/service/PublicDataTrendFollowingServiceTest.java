package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowListDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@SpringBootTest
class PublicDataTrendFollowingServiceTest {

    @Autowired
    private TrendFollowing trendFollowing;

    @Test
    @DisplayName("평균이동선을 구한 값이 나와야 한다.")
    void get() {
        //given
        String samsung = "삼성전자";
        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        //when
        TrendFollowDto date = trendFollowing.getOneday(samsung, nowDate);

        //then
        if( date.getTrendFollowPrice() < date.getBaseDateClosePrice()){
            Assertions.assertThat(date.isBuy()).isEqualTo(true);
        }else{
            Assertions.assertThat(date.isBuy()).isEqualTo(false);
        }
    }

    @Test
    @DisplayName("평균이동선을 구한 값의 리스트의 첫번째 값과 One의 값이 같아야 한다.")
    void getDaysByBaseDt() {
        //given
        String samsung = "삼성전자";
        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        TrendFollowDto daysByBaseDt = trendFollowing.getOneday(samsung, nowDate);

        //when
        TrendFollowListDto dto = trendFollowing.getDaysByBaseDt(samsung, nowDate);

        //then
        Assertions.assertThat(daysByBaseDt.getTrendFollowPrice()).isEqualTo(dto.getTrendFollowPrices().get(0));
    }
}