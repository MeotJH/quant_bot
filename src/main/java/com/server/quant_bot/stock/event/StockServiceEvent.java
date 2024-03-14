package com.server.quant_bot.stock.event;

import com.server.quant_bot.comm.exception.ResourceCommException;
import com.server.quant_bot.stock.enums.StockType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StockServiceEvent {
    private StockType type;


    public StockServiceEvent(String type){
        // ex) type = "korea"
        this.type = getStockType(type);
    }

    private StockType getStockType(String type) {

        for (StockType stockType : StockType.values()) {
            if (stockType.TYPE.equals(type)) {
                return stockType;
            }
        }

        throw new ResourceCommException("해당하는 서비스가 없습니다.");
    }
}
