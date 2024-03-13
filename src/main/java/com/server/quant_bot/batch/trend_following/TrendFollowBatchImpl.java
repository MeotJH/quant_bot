package com.server.quant_bot.batch.trend_following;


import com.server.quant_bot.batch.trend_following.dto.TrendFollowBatchDto;
import com.server.quant_bot.comm.utill.DateUtill;
import com.server.quant_bot.quant.notification.entity.Notification;
import com.server.quant_bot.quant.notification.repository.NotificationRepository;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import com.server.quant_bot.quant.trend_following.repository.TrendFollowRepository;
import com.server.quant_bot.quant.trend_following.service.TrendFollowing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Component
public class TrendFollowBatchImpl implements TrendFollowBatch{

    private final TrendFollowRepository trendFollowRepository;
    private final NotificationRepository notificationRepository;
    private final TrendFollowing trendFollowing;


    @Scheduled(cron = "0 0 16 * * *") // Schedule to run every day at 16:00
    @Override
    public void doJob() {
        /**
         * 1. 모든 유저의 TrendFollow를 가져온다. List<TrendFollow> findTrendFollowAll
         * 2. TrendFollow객체의 StockCode로 금일 추세평균가와 금일 종가를 가져와 비교할 데이터들 리스트를 가져온다 createCompares
         *      2.1 오늘의 매수여부를 판단한다. isBuyToday
         * 3. 저장했던 값과 새로 비교한 값의 isBuy Status가 다른지 확인한다. setNotifySignal
         *  3.1 Status가 다르다면 해당 TrendFollow -> Notification 객체 Status에 True라고 값을 넣는다. saveStatus
         */
        List<TrendFollow> trendFollowAll = findTrendFollowAll();
        List<TrendFollowBatchDto> compares = createCompares(trendFollowAll);
        setNotifySignal(compares);

    }

    private List<TrendFollow> findTrendFollowAll(){
        return trendFollowRepository.findAll();
    }

    private List<TrendFollowBatchDto> createCompares (List<TrendFollow> trendFollowAll){
        List<TrendFollowBatchDto> dtos = new ArrayList<>();
        trendFollowAll.forEach( each -> {
            //알림이 True라면
            if(isNotificationApproval(each)){
                TrendFollowDto today = trendFollowing.getOneday(each.getStock().getStockName(), DateUtill.getToday());
                TrendFollowBatchDto dto = new TrendFollowBatchDto(
                        each.getId()
                        , each.getIsBuy()
                        , today.getIsBuy()
                );
                dtos.add(dto);
                doLog(each,today);
            }

        });

        return dtos;
    }

    private Boolean isNotificationApproval(TrendFollow entity){
        return entity.getNotification().getApproval();
    }


    private void setNotifySignal(List<TrendFollowBatchDto> compares){
        compares.forEach( each -> {
            Boolean status = each.savedIsBuy().equals( each.todayIsBuy() ) ? false : true ;
            saveStatus(each,status);
            doLog(each,status);
        });

    }

    private void saveStatus(TrendFollowBatchDto each, Boolean status){
        Optional<TrendFollow> opt = trendFollowRepository.findById(each.trendFollowId());
        opt.ifPresent( present -> {
            Notification notification = present.getNotification();
            notification = notification.addStatus(status);
            notificationRepository.save(notification);
        });

    }

    private void doLog(TrendFollow saved, TrendFollowDto today){
        log.info("PK: {} TrendFollow's saved TrendFollow :{} , saved ClosingPrice :{} ,saved isBuy :{} ,",saved.getId(),saved.getTrendFollowPrice(),saved.getBaseDateClosePrice(),saved.getIsBuy());
        log.info("PK: {} TrendFollow's today TrendFollow :{} , today ClosingPrice :{} ,today isBuy :{} ,",saved.getId(),today.getTrendFollowPrice(),today.getBaseDateClosePrice(),today.getIsBuy());
    }

    private void doLog(TrendFollowBatchDto dto,Boolean status){
        log.info("PK: {} TrendFollow's Notification Status is {}", dto.trendFollowId() ,status);
    }
}
