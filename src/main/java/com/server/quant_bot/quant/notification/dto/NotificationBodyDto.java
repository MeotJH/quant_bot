package com.server.quant_bot.quant.notification.dto;

public record NotificationBodyDto(
        String stockName
        , Boolean savedIsBuy
        , String todayEnd
        , String todayTrendFollowPrice
        , Boolean todayIsBuy) {
}
