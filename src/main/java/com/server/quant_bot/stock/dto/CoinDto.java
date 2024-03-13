package com.server.quant_bot.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@ToString
public class CoinDto {
    private String status;
    private Map<String, Object> data = new HashMap<>();
    private Map<String, CoinDetail> coinDetails = new HashMap<>();
    private String date;

    @Getter
    @Setter
    @ToString
    public static class CoinDetail {
        @JsonProperty("opening_price")
        private String openingPrice;

        @JsonProperty("closing_price")
        private String closingPrice;

        @JsonProperty("min_price")
        private String minPrice;

        @JsonProperty("max_price")
        private String maxPrice;

        @JsonProperty("units_traded")
        private String unitsTraded;

        @JsonProperty("acc_trade_value")
        private String accTradeValue;

        @JsonProperty("prev_closing_price")
        private String prevClosingPrice;

        @JsonProperty("units_traded_24H")
        private String unitsTraded24H;

        @JsonProperty("acc_trade_value_24H")
        private String accTradeValue24H;

        @JsonProperty("fluctate_24H")
        private String fluctate24H;

        @JsonProperty("fluctate_rate_24H")
        private String fluctateRate24H;
    }
}