package com.server.quant_bot.quant.notification.controller;

import com.server.quant_bot.quant.notification.dto.NotiReqDto;
import com.server.quant_bot.quant.notification.dto.NotificationViewDto;
import com.server.quant_bot.quant.notification.entity.Notification;
import com.server.quant_bot.quant.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/v1/")
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping("/notifications")
    public ResponseEntity<List> findNotificationByUser(){
        return new ResponseEntity<>(notificationService.findByUser(), HttpStatus.OK);
    }

    @PostMapping("/notification/on")
    public Boolean onNotification(@RequestBody NotiReqDto notiReqDto){
        return notificationService.on(notiReqDto) != null ? true : false;
    }

    @PostMapping("/notification/off")
    public Boolean offNotification(@RequestBody NotiReqDto notiReqDto){
        return notificationService.off(notiReqDto) != null ? true : false;
    }
}
