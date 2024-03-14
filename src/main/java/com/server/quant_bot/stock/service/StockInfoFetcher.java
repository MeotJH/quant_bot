package com.server.quant_bot.stock.service;

import com.server.quant_bot.stock.dto.CoinAllInfoDto;
import com.server.quant_bot.stock.dto.CoinCandleDto;

public interface StockInfoFetcher {
    default CoinAllInfoDto getAll(){return null;}

    default CoinCandleDto getByTimeSeries(String ticker) {return null;}
}
