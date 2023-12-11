package com.server.quant_bot.quant.notification.service;

import com.server.quant_bot.comm.exception.ResourceCommException;
import com.server.quant_bot.korea.entity.Stock;
import com.server.quant_bot.korea.service.StockService;
import com.server.quant_bot.quant.notification.dto.NotiReqDto;
import com.server.quant_bot.quant.notification.dto.NotificationMapperDto;
import com.server.quant_bot.quant.notification.entity.Notification;
import com.server.quant_bot.quant.notification.repository.NotificationRepository;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import com.server.quant_bot.quant.trend_following.service.TrendFollowing;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class NotificationImpl implements NotificationService {

    private static final boolean NOTI_ON = true;
    private static final boolean NOTI_OFF = true;

    private final TrendFollowing trendFollowing;
    private final StockService stockService;

    private final NotificationRepository notificationRepository;

    @Override
    public Notification on(NotiReqDto notiReqDto) {
        TrendFollow trendFollow = makeTrendFollowForSave(notiReqDto);
        Notification notification = (Notification) new Notification().update(new NotificationMapperDto(NOTI_ON,trendFollow));
        return notificationRepository.save(notification);
    }

    @Override
    public Notification off(NotiReqDto notiReqDto) {
        TrendFollow trendFollow = makeTrendFollowForSave(notiReqDto);
        Notification notification = (Notification) new Notification().update(new NotificationMapperDto(NOTI_OFF,trendFollow));
        return notificationRepository.save(notification);
    }

    private TrendFollow makeTrendFollowForSave(NotiReqDto notiReqDto) {
        Optional<Stock> stockByStockCode = stockService.findStockByStockCode(notiReqDto.stock());

        if(!stockByStockCode.isPresent()){
            throw new ResourceCommException("Stock 없어서 에러발생.");
        }
        Optional<TrendFollow> trendFollowOpt = trendFollowing.findByStock(stockByStockCode.get());
        if(!trendFollowOpt.isPresent()){
            throw new ResourceCommException("관련 추세투자데이터 없음으로 에러발생.");
        }
        return trendFollowOpt.get();
    }
}
