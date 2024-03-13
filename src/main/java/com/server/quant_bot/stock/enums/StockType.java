package com.server.quant_bot.stock.enums;

import lombok.Getter;
public enum StockType {

    KOREA_STOCK("publicDataStockService"),
    COIN("coinStockService");

    public String TYPE;

    StockType(String type){
        this.TYPE = type;
    }
}
