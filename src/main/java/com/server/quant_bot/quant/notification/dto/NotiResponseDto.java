package com.server.quant_bot.quant.notification.dto;

import java.time.LocalDateTime;

public record NotiResponseDto(
        String stockCode
        , String stockName
        , LocalDateTime notiTime) {
}
