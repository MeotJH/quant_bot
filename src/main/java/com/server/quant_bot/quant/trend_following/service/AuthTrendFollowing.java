package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AuthTrendFollowing {

    default Optional<TrendFollowDto> save(TrendFollowDto dto){ return Optional.ofNullable(null); }

    default List<TrendFollowDto> findTrendDtoByUserId(){return null;}

}
