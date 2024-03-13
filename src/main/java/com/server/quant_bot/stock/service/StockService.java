package com.server.quant_bot.stock.service;

import com.server.quant_bot.stock.dto.PublicDataStockDto;
import com.server.quant_bot.stock.entity.Stock;
import com.server.quant_bot.stock.mapping.StockMapping;

import java.util.List;
import java.util.Optional;

public interface StockService<E> {

    default List<PublicDataStockDto> get(String ticker){return null;}

    default List<PublicDataStockDto> getAllByAfterBeginDate(String ticker,String beginDt){return null;}

    default List<E> FetchToDB(){ return null; }

    default Optional<Stock> findStockByStockCode(String stockCode) {return Optional.ofNullable(null);}

    default List<StockMapping> getStocksByStockLike(String keyword){ return null;}
}
