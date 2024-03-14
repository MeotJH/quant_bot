package com.server.quant_bot.stock.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StockDto {
    private Double closingPrice;
    private String baseDate;
}
