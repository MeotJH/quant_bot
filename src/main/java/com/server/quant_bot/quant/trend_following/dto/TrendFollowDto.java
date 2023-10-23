package com.server.quant_bot.quant.trend_following.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrendFollowDto {

    private double trendFollowPrice;
    private double baseDateClosePrice;
    private boolean isBuy;

}
