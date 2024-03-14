package com.server.quant_bot.stock.enums;

import lombok.Getter;
public enum StockType {

    KOREA_STOCK("korea","publicDataStockService","publicDataTrendFollowingService"),
    COIN("coin","coinStockService","coinTrendFollowingService");

    public String TYPE;

    public String STOCK_SERVICE;

    public String TREND_SERVICE;

    StockType(String type, String stockService, String trendService){
        this.TYPE = type;
        this.STOCK_SERVICE = stockService;
        this.TREND_SERVICE = trendService;
    }
}
