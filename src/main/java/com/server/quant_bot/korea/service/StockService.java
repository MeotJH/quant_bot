package com.server.quant_bot.korea.service;

import com.server.quant_bot.korea.dto.PublicDataStockDto;
import com.server.quant_bot.korea.entity.Stock;
import com.server.quant_bot.korea.mapping.StockMapping;
import com.server.quant_bot.korea.repository.StockRepository;

import java.util.List;
import java.util.Optional;

public interface StockService {

    default List<PublicDataStockDto> get(String ticker){return null;}

    default List<PublicDataStockDto> getAllByAfterBeginDate(String ticker,String beginDt){return null;}

    default List<Stock> CSVToDB(){ return null; }

    default Optional<Stock> findStockByStockCode(String stockCode) {return Optional.ofNullable(null);}

    default List<StockMapping> getStocksByStockLike(String keyword){ return null;}
}
