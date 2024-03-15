package com.server.quant_bot.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpbitMarketCoinsDto {

    @JsonProperty("market")
    private String market;

    @JsonProperty("korean_name")
    private String stockNames;

    @JsonProperty("english_name")
    private String stockEngName;
}
