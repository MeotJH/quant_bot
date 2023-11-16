package com.server.quant_bot.quant.trend_following.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrendFollowDto {

    private String trendFollowPrice;
    private String baseDateClosePrice;
    private boolean isBuy;

}
