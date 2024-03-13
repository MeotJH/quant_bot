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

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2/trend-follow")
public class TrendFollowV2Controller {

    private final TrendFollowing trendFollowing;

    @GetMapping("/{type}/{ticker}")
    public TrendFollowDto getStockTrendFollow(@PathVariable String type , @PathVariable String ticker){
        return trendFollowing.getOneday(ticker, DateUtill.getToday());
    }

    @GetMapping("/{type}/{ticker}/list")
    public TrendFollowListDto getStockTrendFollows(@PathVariable String type ,@PathVariable String ticker){
        return trendFollowing.getDays(ticker, DateUtill.getToday());
    }
}
