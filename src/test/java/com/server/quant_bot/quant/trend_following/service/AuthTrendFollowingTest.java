package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.comm.security.service.UserService;
import com.server.quant_bot.korea.entity.Stock;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@WithMockUser(username = "userId")
@Transactional(rollbackFor = Exception.class)
class AuthTrendFollowingImplTest {

    @Autowired
    AuthTrendFollowing authTrendFollowing;

    @Autowired
    UserService userService;
    
    @Test
    @DisplayName("유저를 저장한 후 저장된 값과 저장했던 값이 같아야 한다.")
    void saveTest() {
        //given
        TrendFollowDto dto = TrendFollowDto
                                .builder()
                                    .stock("JYP Ent.")
                                    .isBuy(false)
                                    .baseDateClosePrice("104,972.34")
                                    .trendFollowPrice("97,100")
                                .build();
        //when
        Optional<TrendFollow> saved = authTrendFollowing.save(dto);

        //then
        Assertions.assertThat(saved.isPresent()).isTrue();
        Assertions.assertThat(saved.get().getStock().getStockName()).isEqualTo(dto.getStock());
        Assertions.assertThat(saved.get().getIsBuy()).isEqualTo(dto.getIsBuy());
        Assertions.assertThat(saved.get().getBaseDateClosePrice()).isEqualTo(dto.getBaseDateClosePrice());
        Assertions.assertThat(saved.get().getTrendFollowPrice()).isEqualTo(dto.getTrendFollowPrice());
    
    }


    @Test
    @DisplayName("유저ID를 기준으로 DB에서 추세이동선데이터를 가져와야한다.")
    void findTrendDtoByUserIdTest() {
        //given
        TrendFollowDto dto = TrendFollowDto
                .builder()
                .stock("JYP Ent.")
                .isBuy(false)
                .baseDateClosePrice("104,972.34")
                .trendFollowPrice("97,100")
                .build();
        Optional<TrendFollow> save = authTrendFollowing.save(dto);
        save.ifPresent( saved -> log.info(":::this is saved userId = {}" , saved.getUser() ) );

        //when
        List<TrendFollowDto> trendDtoByUserId = authTrendFollowing.findTrendDtoByUserId();
        TrendFollowDto trendFollowDto = trendDtoByUserId.get(0);
        String stock = trendFollowDto.getStock();

        //then
        Assertions.assertThat(trendFollowDto.getIsBuy()).isEqualTo(dto.getIsBuy());
        Assertions.assertThat(trendFollowDto.getTrendFollowPrice()).isEqualTo(dto.getTrendFollowPrice());
        Assertions.assertThat(trendFollowDto.getBaseDateClosePrice()).isEqualTo(dto.getBaseDateClosePrice());
        Assertions.assertThat(trendFollowDto.getStock()).isEqualTo(dto.getStock());

    }
}