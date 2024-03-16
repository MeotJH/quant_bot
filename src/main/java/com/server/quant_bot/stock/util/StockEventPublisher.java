package com.server.quant_bot.stock.util;

import com.server.quant_bot.stock.event.StockServiceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StockEventPublisher {
    private final ApplicationEventPublisher eventPublisher;
    public void processStockData(String type) {
        eventPublisher.publishEvent(new StockServiceEvent(type));
    }
}
