package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowListDto;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;

import java.util.List;
import java.util.Optional;

public interface TrendFollowing {

    default TrendFollowDto getOneday(String ticker, String baseDt){return null;}

    default TrendFollowListDto getDaysByBaseDt(String ticker, String baseDt){return null;}

    default List<String> getStocksByKeyword(String keyword){return null;}

    default Optional<TrendFollow> save(TrendFollowDto dto) {return Optional.ofNullable(null);}
}
