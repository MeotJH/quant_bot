package com.server.quant_bot.batch.trend_following.dto;

import java.util.UUID;

public record TrendFollowBatchDto(
        UUID trendFollowId
        , Boolean savedIsBuy
        , Boolean todayIsBuy) {
}
