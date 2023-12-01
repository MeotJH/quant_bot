package com.server.quant_bot.quant.trend_following.dto;

import com.server.quant_bot.comm.security.entity.UserEntity;
import com.server.quant_bot.korea.entity.Stock;
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
