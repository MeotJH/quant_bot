package com.server.quant_bot.batch.trend_following;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(rollbackFor = Exception.class)
class TrendFollowBatchImplTest {

    @Autowired
    TrendFollowBatch trendFollowBatch;

    @Test
    @DisplayName("추세평균 테스트를 한다.")
    void doJobTest() {
        //given
        trendFollowBatch.doJob();

        //when

        //then

    }

}