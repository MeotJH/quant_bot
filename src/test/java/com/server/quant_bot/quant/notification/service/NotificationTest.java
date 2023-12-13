package com.server.quant_bot.quant.notification.service;

import com.server.quant_bot.quant.notification.dto.NotiReqDto;
import com.server.quant_bot.quant.notification.entity.Notification;
import com.server.quant_bot.quant.notification.repository.NotificationRepository;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.repository.TrendFollowRepository;
import com.server.quant_bot.quant.trend_following.service.TrendFollowing;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@SpringBootTest
@WithMockUser(username = "testId")
@Transactional(rollbackFor = Exception.class)
class NotificationTest {

    @Autowired
    NotificationService notificationService;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    TrendFollowRepository trendFollowRepository;

    @Autowired
    TrendFollowing trendFollowing;

    @Test
    @DisplayName("알림 켜짐이 디비에 저장되어야 한다")
    void onTest() {
        //given
        String stockCd = "035900";
        TrendFollowDto dto = TrendFollowDto
                .builder()
                .stock(stockCd)
                .isBuy(false)
                .baseDateClosePrice("104,972.34")
                .trendFollowPrice("97,100")
                .build();
        Optional<TrendFollowDto> saved = trendFollowing.save(dto);
        NotiReqDto notiReqDto = new NotiReqDto(stockCd);

        //when
        Notification on = notificationService.on(notiReqDto);
        String trendFollowPrice = on.getTrendFollow().getTrendFollowPrice();


        //then
        Assertions.assertThat(trendFollowPrice).isEqualTo(saved.get().getTrendFollowPrice());
        Assertions.assertThat(on.getApproval()).isEqualTo(true);

    }

    @Test
    @DisplayName("알림 꺼짐이 디비에 저장되어야 한다")
    void offTest() {
        //given
        String stockCd = "035900";
        TrendFollowDto dto = TrendFollowDto
                .builder()
                .stock(stockCd)
                .isBuy(false)
                .baseDateClosePrice("104,972.34")
                .trendFollowPrice("97,100")
                .build();
        Optional<TrendFollowDto> saved = trendFollowing.save(dto);
        NotiReqDto notiReqDto = new NotiReqDto(stockCd);

        //when
        Notification off = notificationService.off(notiReqDto);
        String trendFollowPrice = off.getTrendFollow().getTrendFollowPrice();


        //then
        Assertions.assertThat(trendFollowPrice).isEqualTo(saved.get().getTrendFollowPrice());
        Assertions.assertThat(off.getApproval()).isEqualTo(false);

    }
}