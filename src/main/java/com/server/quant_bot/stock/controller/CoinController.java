package com.server.quant_bot.stock.controller;

import com.server.quant_bot.stock.dto.PublicDataStockDto;
import com.server.quant_bot.stock.enums.StockType;
import com.server.quant_bot.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coin")
@RequiredArgsConstructor
public class CoinController {

    @Qualifier("coinStockService")
    private final StockService service;

    @GetMapping("/{ticker}")
    public List<PublicDataStockDto> findStockByTicker(@PathVariable String ticker){
        return service.get(ticker);
    }

    @GetMapping("/stocks/{keyword}")
    public List findStocksByStockLike(@PathVariable String keyword){
        return service.getStocksByStockLike(keyword);
    }
}
