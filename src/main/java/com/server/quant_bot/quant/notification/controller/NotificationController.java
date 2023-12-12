package com.server.quant_bot.quant.notification.controller;

import com.server.quant_bot.quant.notification.dto.NotiReqDto;
import com.server.quant_bot.quant.notification.entity.Notification;
import com.server.quant_bot.quant.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/on")
    public Boolean onNotification(@RequestBody NotiReqDto notiReqDto){
        return notificationService.on(notiReqDto) != null ? true : false;
    }

    @PostMapping("/off")
    public Boolean offNotification(@RequestBody NotiReqDto notiReqDto){
        return notificationService.off(notiReqDto) != null ? true : false;
    }
}
