package com.server.quant_bot.korea.service;


import com.server.quant_bot.korea.dto.MarketDto;

public interface MarketService {

    default MarketDto getData(){ return null;}
}
