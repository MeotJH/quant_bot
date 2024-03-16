package com.server.quant_bot.quant.trend_following.dto;

import com.server.quant_bot.stock.entity.Stock;
import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendFollowDto {

    private String stockName;
    private String trendFollowPrice;
    private String baseDateClosePrice;
    private Boolean isBuy;
    private Boolean approval;
    private String market;
    private Stock stock;

}
