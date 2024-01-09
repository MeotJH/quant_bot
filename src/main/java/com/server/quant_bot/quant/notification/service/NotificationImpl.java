package com.server.quant_bot.quant.notification.service;

import com.server.quant_bot.comm.exception.ResourceCommException;
import com.server.quant_bot.comm.security.entity.UserEntity;
import com.server.quant_bot.comm.security.service.UserService;
import com.server.quant_bot.korea.entity.Stock;
import com.server.quant_bot.korea.service.StockService;
import com.server.quant_bot.quant.notification.dto.*;
import com.server.quant_bot.quant.notification.entity.Notification;
import com.server.quant_bot.quant.notification.repository.NotificationRepository;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import com.server.quant_bot.quant.trend_following.repository.TrendFollowRepository;
import com.server.quant_bot.quant.trend_following.service.TrendFollowing;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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
    private final UserService userService;

    @Override
    public List<NotificationViewDto> findByUser() {
        List<NotificationViewDto> list = new ArrayList<>();
        Optional<UserEntity> userByLoginId = userService.findUserByLoginId();

        //유저가 존재하면 로직 서비스 로직 실행
        if( userByLoginId.isPresent() ){
            UserEntity userEntity = userByLoginId.get();
            //trendFollow + 다른 퀀트들도 넣어줄 수 있음을 염두해서 개발한 소스
            list = fetchTrendFollow(userEntity);
        }
        return list;
    }

    @Override
    public Notification on(NotiReqDto notiReqDto) {
        return process(notiReqDto,NOTI_ON);
    }

    @Override
    public Notification off(NotiReqDto notiReqDto) {
        return process(notiReqDto,NOTI_OFF);
    }

    @Override
    public List<NotiResponseDto> doNotify() {
        Optional<UserEntity> userOpt = userService.findUserByLoginId();
        if(!userOpt.isPresent()){
            new ResourceCommException("유저가 존재하지 않습니다.");
        }

        //값이 없으면 빈값을 리턴한다.
        List<TrendFollow> allByUser = trendFollowRepository.findAllByUser(userOpt.get());
        List<NotiResponseDto> notiResponses = new ArrayList<>();
        if(allByUser.isEmpty()){
            return notiResponses;
        }

        for (TrendFollow each: allByUser) {
            //notify status
            Boolean status = each.getNotification().getStatus();
            if(status.equals(true)){
                Stock stock = each.getStock();
                notiResponses.add(
                        new NotiResponseDto(
                            stock.getStockCode()
                            , stock.getStockName()
                            , each.getNotification().getUpdateDate()
                        )
                );
            }

        }

        return notiResponses;
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

    private List<NotificationViewDto> fetchTrendFollow(UserEntity userEntity) {
        List<NotificationViewDto> list = new ArrayList<>();
        Iterator<TrendFollow> iterator = userEntity.getTrendFollows().iterator();
        while(iterator.hasNext()){
            TrendFollow trendFollow = iterator.next();
            Notification notification = trendFollow.getNotification();

            if(notification.getApproval()){
                list.add(
                        new NotificationViewDto(
                                trendFollow.getClass().getName()

                                , new NotificationBodyDto(
                                      trendFollow.getStock().getStockName()
                                    , trendFollow.getIsBuy()
                                    , "todayEnd"
                                    , "todayTrendFollowPrice"
                                    , false
                                    )

                                , notification.getUpdateDate()
                        )
                );
            }
        }
        return list;
    }
}
