package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AuthTrendFollowing {

    default Optional<TrendFollow> save(TrendFollowDto dto){ return Optional.ofNullable(null); }

}
