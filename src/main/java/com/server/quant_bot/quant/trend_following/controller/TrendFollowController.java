package com.server.quant_bot.quant.trend_following.controller;

import com.server.quant_bot.comm.utill.DateUtill;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowListDto;
import com.server.quant_bot.quant.trend_following.service.TrendFollowing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/trend-follow")
public class TrendFollowController {

    private final TrendFollowing trendFollowing;

    @GetMapping("/{ticker}")
    public TrendFollowDto getStockTrendFollow(@PathVariable String ticker){
        return trendFollowing.getOneday(ticker, DateUtill.getToday());
    }

    @GetMapping("/{ticker}/list")
    public TrendFollowListDto getStockTrendFollows(@PathVariable String ticker){
        return trendFollowing.getDaysByBaseDt(ticker, DateUtill.getToday());
    }
}
