package com.server.quant_bot.korea.service;

import com.server.quant_bot.korea.dto.PublicDataStockDto;
import com.server.quant_bot.korea.entity.Stock;
import com.server.quant_bot.korea.repository.StockRepository;

import java.util.List;

public interface StockService {

    default List<PublicDataStockDto> get(String ticker){return null;}

    default List<PublicDataStockDto> getAllByAfterBeginDate(String ticker,String beginDt){return null;}

    default List<Stock> CSVToDB(){ return null; }
}
