package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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
        TrendFollowDto date = trendFollowing.get(samsung, nowDate);

        //then
        if( date.getTrendFollowPrice() < date.getBaseDateClosePrice()){
            Assertions.assertThat(date.isBuy()).isEqualTo(true);
        }else{
            Assertions.assertThat(date.isBuy()).isEqualTo(false);
        }
    }
}