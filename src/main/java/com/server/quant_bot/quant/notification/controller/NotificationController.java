package com.server.quant_bot.quant.notification.controller;

import com.server.quant_bot.quant.notification.dto.NotiReqDto;
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
    public void onNotification(@RequestBody NotiReqDto notiReqDto){
        notificationService.on(notiReqDto);
    }

    @PostMapping("/off")
    public void offNotification(@RequestBody NotiReqDto notiReqDto){
        notificationService.off(notiReqDto);
    }
}
