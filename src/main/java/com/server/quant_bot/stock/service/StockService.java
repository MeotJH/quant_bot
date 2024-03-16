package com.server.quant_bot.stock.service;

import com.server.quant_bot.stock.dto.PublicDataStockDto;
import com.server.quant_bot.stock.entity.Stock;
import com.server.quant_bot.stock.mapping.StockMapping;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface StockService<E> {

    default List<E> get(String ticker){return null;}

    default List<E> getAllByAfterBeginDate(String ticker,String beginDt){return null;}

    default List<E> FetchToDB() throws IOException { return null; }

    default Optional<E> findStockByStockCode(String stockCode) {return Optional.ofNullable(null);}

    default List<E> getStocksByStockLike(String keyword){ return null;}
}
