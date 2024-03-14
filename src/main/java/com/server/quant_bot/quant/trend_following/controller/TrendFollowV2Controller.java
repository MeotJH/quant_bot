package com.server.quant_bot.quant.trend_following.controller;

import com.server.quant_bot.comm.utill.DateUtill;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowListDto;
import com.server.quant_bot.quant.trend_following.service.TrendFollowing;
import com.server.quant_bot.stock.event.StockServiceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2")
public class TrendFollowV2Controller {

    private final TrendFollowing trendFollowing;
    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("/trend-follow/{type}/{ticker}")
    public TrendFollowDto getStockTrendFollow(@PathVariable String type , @PathVariable String ticker){
        processStockData(type); //TODO 중복이 좀더 늘어나면 AOP로 바꿔야하지 않을까?
        return trendFollowing.getOneday(ticker, DateUtill.getToday());
    }

    @GetMapping("/trend-follows/{type}/{ticker}")
    public TrendFollowListDto getStockTrendFollows(@PathVariable String type ,@PathVariable String ticker){
        processStockData(type);
        return trendFollowing.getDays(ticker, DateUtill.getToday());
    }

    public void processStockData(String type) {
        eventPublisher.publishEvent(new StockServiceEvent(type));
    }
}
