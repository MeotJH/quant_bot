package com.server.quant_bot.quant.trend_following.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TrendFollowListDto {

    private List<Double> trendFollowPrices = new ArrayList<>();
    private List<Double> ClosePrice= new ArrayList<>();
    private List<Boolean> isBuy= new ArrayList<>();
    private List<String> baseDt= new ArrayList<>();
}
