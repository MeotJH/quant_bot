package com.server.quant_bot.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class UpbitMarketCoinsDto {

    private String ticker;

    private String stockNames;

    private String stockEngName;
}
