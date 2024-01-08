package com.server.quant_bot.quant.notification.service;

import com.server.quant_bot.quant.notification.dto.NotiReqDto;
import com.server.quant_bot.quant.notification.dto.NotiResponseDto;
import com.server.quant_bot.quant.notification.dto.NotificationViewDto;
import com.server.quant_bot.quant.notification.entity.Notification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public interface NotificationService {

    default List<NotificationViewDto> findByUser(){return new ArrayList<>();}

    default Notification on(NotiReqDto notiReqDto){return null;}

    default Notification off(NotiReqDto notiReqDto){return null;}

    default List<NotiResponseDto> doNotify(){return null;}
}
