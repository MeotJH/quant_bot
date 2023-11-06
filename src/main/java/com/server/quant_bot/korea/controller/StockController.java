package com.server.quant_bot.korea.controller;

import com.server.quant_bot.korea.dto.PublicDataStockDto;
import com.server.quant_bot.korea.service.MarketService;
import com.server.quant_bot.korea.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.server.quant_bot.korea.dto.MarketDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/korea")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;
    private final MarketService marketService;

    @GetMapping("/{ticker}")
    public List<PublicDataStockDto> findStockByTicker(@PathVariable String ticker){
        return stockService.get(ticker);
    }

    @GetMapping("/market")
    public MarketDto findMarket(){
        return marketService.getData();
    }
}
