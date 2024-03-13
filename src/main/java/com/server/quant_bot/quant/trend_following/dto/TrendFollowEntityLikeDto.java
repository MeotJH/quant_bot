package com.server.quant_bot.quant.trend_following.dto;

import com.server.quant_bot.comm.security.entity.UserEntity;
import com.server.quant_bot.stock.entity.Stock;
import com.server.quant_bot.quant.notification.entity.Notification;

public record TrendFollowEntityLikeDto (
        UserEntity user
        , Stock stock
        , String trendFollowPrice
        , Boolean isBuy
        , String baseDateClosePrice
        , Notification notification
){

}
