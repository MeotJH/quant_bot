package com.server.quant_bot.quant.trend_following.controller;

import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowUserPageDto;
import com.server.quant_bot.quant.trend_following.service.TrendFollowing;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth/v1/trend-follow")
public class AuthTrendFollowController {

    private final TrendFollowing trendFollowing;

    @GetMapping
    public List<TrendFollowUserPageDto> findTrendFollowsByUser(){
        return trendFollowing.findTrendDtoByUserId();
    }

    @PostMapping
    public TrendFollowDto saveTrendFollow(@RequestBody TrendFollowDto dto){
        log.info("data is :: {}",dto.toString());
        return trendFollowing.save(dto).get();
    }
}
