package com.server.quant_bot.korea.service;

import com.server.quant_bot.korea.dto.PublicDataStockDto;

import java.util.List;

public interface StockService {

    default List<PublicDataStockDto> get(String ticker){return null;}
}
