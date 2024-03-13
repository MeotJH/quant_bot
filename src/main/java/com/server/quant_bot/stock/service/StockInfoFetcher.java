package com.server.quant_bot.stock.service;

import com.server.quant_bot.stock.dto.CoinDto;

import java.util.List;

public interface StockInfoFetcher {
    default CoinDto getAll(){return null;}
}
