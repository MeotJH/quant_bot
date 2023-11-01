package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowListDto;

import java.util.List;

public interface TrendFollowing {

    default TrendFollowDto getOneday(String ticker, String baseDt){return null;}

    default TrendFollowListDto getDaysByBaseDt(String ticker, String baseDt){return null;}
}
