package com.server.quant_bot.quant.trend_following.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendFollowDto {

    private String stock;
    private String trendFollowPrice;
    private String baseDateClosePrice;
    private Boolean isBuy;

}
