package com.server.quant_bot.stock.controller;

import com.server.quant_bot.stock.dto.PublicDataStockDto;
import com.server.quant_bot.stock.service.MarketService;
import com.server.quant_bot.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.server.quant_bot.stock.dto.MarketDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/korea")
@RequiredArgsConstructor
public class KoreaStockController {

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

    @GetMapping("/stocks/{keyword}")
    public List findStocksByStockLike(@PathVariable String keyword){
        return stockService.getStocksByStockLike(keyword);
    }
}
