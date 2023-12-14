package com.server.quant_bot.batch.trend_following;


import com.server.quant_bot.batch.trend_following.dto.TrendFollowBatchDto;
import com.server.quant_bot.comm.utill.DateUtill;
import com.server.quant_bot.korea.entity.Stock;
import com.server.quant_bot.quant.notification.entity.Notification;
import com.server.quant_bot.quant.notification.repository.NotificationRepository;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowListDto;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowRecord;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import com.server.quant_bot.quant.trend_following.repository.TrendFollowRepository;
import com.server.quant_bot.quant.trend_following.service.TrendFollowing;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Component
public class TrendFollowBatchImpl implements TrendFollowBatch{

    private final TrendFollowRepository trendFollowRepository;
    private final NotificationRepository notificationRepository;
    private final TrendFollowing trendFollowing;


    //@Scheduled(cron = "0 0 16 * * *") // Schedule to run every day at 16:00
    @Transactional(rollbackFor = Exception.class)
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
        List<TrendFollow> all = trendFollowRepository.findAll();
        return trendFollowRepository.findAll();
    }

    private List<TrendFollowBatchDto> createCompares (List<TrendFollow> trendFollowAll){
        List<TrendFollowBatchDto> dtos = new ArrayList<>();
        trendFollowAll.forEach( each -> {
            TrendFollowDto today = trendFollowing.getOneday(each.getStock().getStockName(), DateUtill.getToday());
            TrendFollowBatchDto dto = new TrendFollowBatchDto(
                                              each.getId()
                                            , each.getIsBuy()
                                            , today.getIsBuy()
                                            );
            dtos.add(dto);
        });

        return dtos;
    }

    private void setNotifySignal(List<TrendFollowBatchDto> compares){
        compares.forEach( each -> {
            Boolean status = each.savedIsBuy().equals( each.todayIsBuy() ) ? false : true ;
            saveStatus(each,status);
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

    /**
     *
     * @param today
     * @return 오늘 구매해야 하는지 여부
     */
    private Boolean isBuyToday(TrendFollowDto today){
        Double trendFollowPrice = Double.valueOf( today.getTrendFollowPrice().replaceAll(",","") );
        Double baseDateClosePrice = Double.valueOf( today.getBaseDateClosePrice().replaceAll(",","") );
        return trendFollowPrice >= baseDateClosePrice ? true : false;
    }
}
