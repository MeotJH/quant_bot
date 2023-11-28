package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(rollbackFor = Exception.class)
class AuthTrendFollowingImplTest {

    @Autowired
    AuthTrendFollowing authTrendFollowing;
    
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
        Assertions.assertThat(saved.get().getStock()).isEqualTo(dto.getStock());
        Assertions.assertThat(saved.get().getIsBuy()).isEqualTo(dto.getIsBuy());
        Assertions.assertThat(saved.get().getBaseDateClosePrice()).isEqualTo(dto.getBaseDateClosePrice());
        Assertions.assertThat(saved.get().getTrendFollowPrice()).isEqualTo(dto.getTrendFollowPrice());
    
    }
}