package com.server.quant_bot.quant.trend_following.controller;

import com.server.quant_bot.comm.utill.DateUtill;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.service.TrendFollowing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trend-follow")
public class TrendFollowController {

    private final TrendFollowing trendFollowing;

    @GetMapping("/{ticker}")
    public TrendFollowDto getStockTrendFollow(@PathVariable String ticker){
        return trendFollowing.get(ticker, DateUtill.getToday());
    }
}
