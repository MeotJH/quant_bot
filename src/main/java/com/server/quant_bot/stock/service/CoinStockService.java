package com.server.quant_bot.stock.service;

import com.server.quant_bot.stock.dto.CoinDto;
import com.server.quant_bot.stock.dto.PublicDataStockDto;
import com.server.quant_bot.stock.entity.Coin;
import com.server.quant_bot.stock.entity.Stock;
import com.server.quant_bot.stock.mapping.StockMapping;
import com.server.quant_bot.stock.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CoinStockService<E> implements StockService{

    private final StockInfoFetcher stockInfoFetcher;
    private final CoinRepository coinRepository;

    @Override
    public List<PublicDataStockDto> get(String ticker) {
        return StockService.super.get(ticker);
    }

    @Override
    public List<E> FetchToDB() {
        List<E> all = (List<E>) coinRepository.findAll();

        if( !all.isEmpty() ){
            return all;
        }

        for (Map.Entry<String, CoinDto.CoinDetail> entry : stockInfoFetcher.getAll().getCoinDetails().entrySet()){
            all.add(
                    (E) coinRepository.save(
                            new Coin().toEntity(
                                    entry.getValue()
                            )
                    )
            );
        }

        return all;
    }

    @Override
    public Optional<Stock> findStockByStockCode(String stockCode) {
        return StockService.super.findStockByStockCode(stockCode);
    }

    @Override
    public List<StockMapping> getStocksByStockLike(String keyword) {
        return StockService.super.getStocksByStockLike(keyword);
    }
}
