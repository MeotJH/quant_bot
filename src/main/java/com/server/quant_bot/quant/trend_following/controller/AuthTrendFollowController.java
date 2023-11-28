package com.server.quant_bot.quant.trend_following.controller;

import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.service.TrendFollowing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth/v1/trend-follow")
public class AuthTrendFollowController {

    private final TrendFollowing trendFollowing;

    @PostMapping
    public void saveTrendFollow(@RequestBody TrendFollowDto dto){
        log.info("data is :: {}",dto.toString());
        trendFollowing.save(dto);
    }
}
