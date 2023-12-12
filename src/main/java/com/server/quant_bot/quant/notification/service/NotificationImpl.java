package com.server.quant_bot.quant.notification.service;

import com.server.quant_bot.comm.entity.BaseEntity;
import com.server.quant_bot.comm.exception.ResourceCommException;
import com.server.quant_bot.korea.entity.Stock;
import com.server.quant_bot.korea.service.StockService;
import com.server.quant_bot.quant.notification.dto.NotiReqDto;
import com.server.quant_bot.quant.notification.dto.NotificationMapperDto;
import com.server.quant_bot.quant.notification.entity.Notification;
import com.server.quant_bot.quant.notification.repository.NotificationRepository;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import com.server.quant_bot.quant.trend_following.repository.TrendFollowRepository;
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
    private static final boolean NOTI_OFF = false;

    private final TrendFollowing trendFollowing;
    private final StockService stockService;

    private final TrendFollowRepository trendFollowRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public Notification on(NotiReqDto notiReqDto) {
        return process(notiReqDto,NOTI_ON);
    }

    @Override
    public Notification off(NotiReqDto notiReqDto) {
        return process(notiReqDto,NOTI_OFF);
    }

    private Notification process(NotiReqDto notiReqDto, Boolean notiStatus) {
        Optional<Stock> stockByStockCode = stockService.findStockByStockCode(notiReqDto.stock());

        if(!stockByStockCode.isPresent()){
            throw new ResourceCommException("Stock 없어서 에러발생.");
        }
        Optional<TrendFollow> trendFollowOpt = trendFollowing.findByStock(stockByStockCode.get());
        if(!trendFollowOpt.isPresent()){
            throw new ResourceCommException("관련 추세투자데이터 없음으로 에러발생.");
        }
        TrendFollow trendFollow = trendFollowOpt.get();

        Notification notification = trendFollow.getNotification() == null ? new Notification() : trendFollow.getNotification();
        Notification update = (Notification) notification.update(new NotificationMapperDto(notiStatus, trendFollow));
        trendFollow.addNoti(update);
        Notification save = notificationRepository.save(notification);
        trendFollowRepository.save(trendFollow);
        return save;
    }
}
