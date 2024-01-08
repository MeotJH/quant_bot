package com.server.quant_bot.quant.notification.dto;

import java.time.LocalDateTime;

public record NotificationViewDto(String title, NotificationBodyDto body, LocalDateTime time) {
}
