package com.server.quant_bot.quant.notification.dto;

import com.server.quant_bot.quant.trend_following.entity.TrendFollow;

import java.util.UUID;


public record NotificationMapperDto(boolean approval, TrendFollow trendFollow) {
}
