package com.server.quant_bot.stock.enums;

import com.server.quant_bot.comm.exception.ResourceCommException;
import lombok.Getter;
public enum StockType {

    KOREA_STOCK("korea", new String[]{"KOSPI", "KOSDAQ", "KOSPI GLOBAL", "KOSDAQ GLOBAL"},"publicDataStockService","publicDataTrendFollowingService"),
    COIN("coin",new String[]{"CRYPTO"},"coinStockService","coinTrendFollowingService");

    public String TYPE;

    public String[] MARKET;

    public String STOCK_SERVICE;

    public String TREND_SERVICE;

    StockType(String type, String[] market, String stockService, String trendService){
        this.TYPE = type;
        this.MARKET = market;
        this.STOCK_SERVICE = stockService;
        this.TREND_SERVICE = trendService;
    }

    public static String getStockServiceByType(String market){
        for (StockType type : StockType.values()) {
            for (String each: type.MARKET) {
                if(each.equals(market)){
                    return type.STOCK_SERVICE;
                }
            }
        }

        throw new ResourceCommException("맞는 Enum이 없습니다. StockType");
    }
}
