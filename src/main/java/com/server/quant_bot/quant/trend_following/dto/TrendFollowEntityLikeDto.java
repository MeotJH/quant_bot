package com.server.quant_bot.quant.trend_following.dto;

import com.server.quant_bot.comm.security.entity.UserEntity;
import com.server.quant_bot.korea.entity.Stock;
import com.server.quant_bot.quant.trend_following.mapper.TrendFollowEnntityLikeMapper;

public record TrendFollowEntityLikeDto (
        UserEntity user
        ,Stock stock
        ,String trendFollowPrice
        ,Boolean isBuy
        ,String baseDateClosePrice
){

}
