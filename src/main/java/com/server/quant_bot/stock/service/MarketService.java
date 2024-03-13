package com.server.quant_bot.stock.service;


import com.server.quant_bot.stock.dto.MarketDto;

public interface MarketService {

    default MarketDto getData(){ return null;}
}
