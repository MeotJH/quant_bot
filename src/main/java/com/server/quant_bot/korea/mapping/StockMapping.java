package com.server.quant_bot.korea.mapping;

/**
 * Entity 로 리턴받지 않고 필요한 데이터만 받아 오기 위해 작성한 interface
 * Spring JPA FrameWork의 구현방법
 */
public interface StockMapping {
    String getStockName();
    String getStockCode();
}
