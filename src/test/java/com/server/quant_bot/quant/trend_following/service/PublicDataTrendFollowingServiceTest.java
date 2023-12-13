package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowListDto;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowUserPageDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@WithMockUser(username = "userId")
@Transactional(rollbackFor = Exception.class)
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
        Double trendFollow = Double.parseDouble(date.getTrendFollowPrice().replaceAll(",", ""));
        Double baseDateClosePrice = Double.parseDouble(date.getBaseDateClosePrice().replaceAll(",", ""));

        //then
        if( trendFollow < baseDateClosePrice ){
            Assertions.assertEquals(date.getIsBuy(),true);
        }else{
            Assertions.assertEquals(date.getIsBuy(),false);
        }
    }

    @Test
    @DisplayName("실패했으니 예외처리 되어야 한다.")
    void getFail() {
        //given
        String samsung = "네이버";
        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        //then
        Assertions.assertThrows(RuntimeException.class, () -> {
            trendFollowing.getOneday(samsung, nowDate);
        });


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
        Assertions.assertEquals(daysByBaseDt.getTrendFollowPrice(),dto.getTrendFollowPrices().get(0));
    }
    
    @Test
    @DisplayName("유저 페이지의 카드를 만들기 위한 평균이동선 dto가 저장되었던 dto 값과 같아야 한다.")
    void findTrendDtoByUserIdTest() {
        //given
        TrendFollowDto dto = TrendFollowDto
                .builder()
                .stock("035900")
                .isBuy(false)
                .baseDateClosePrice("104,972.34")
                .trendFollowPrice("97,100")
                .build();
        TrendFollowDto saved = trendFollowing.save(dto).get();
                
        //when
        TrendFollowUserPageDto userPageDto = trendFollowing.findTrendDtoByUserId().get(0);

        //then
        assertThat(userPageDto.savedTrendFollowPrice()).isEqualTo(saved.getTrendFollowPrice());
        assertThat(userPageDto.savedClosePrice()).isEqualTo(saved.getBaseDateClosePrice());
        assertThat(userPageDto.savedIsBuy()).isEqualTo(saved.getIsBuy());
    
    }
}