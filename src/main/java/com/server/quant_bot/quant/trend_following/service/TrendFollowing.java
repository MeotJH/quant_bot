package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;

public interface TrendFollowing {

    default TrendFollowDto get(String ticker, String baseDt){return null;}
}
