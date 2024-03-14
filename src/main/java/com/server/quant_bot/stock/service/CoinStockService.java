package com.server.quant_bot.stock.service;

import com.server.quant_bot.comm.exception.ResourceCommException;
import com.server.quant_bot.stock.dto.CoinAllInfoDto;
import com.server.quant_bot.stock.dto.CoinCandleDto;
import com.server.quant_bot.stock.entity.Coin;
import com.server.quant_bot.stock.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CoinStockService<E> implements StockService{

    private final StockInfoFetcher stockInfoFetcher;
    private final CoinRepository coinRepository;

    @Override
    public List<E> get(String ticker) {
        return StockService.super.get(ticker);
    }

    @Override
    public List<E> getAllByAfterBeginDate(String ticker, String beginDt) {
        CoinCandleDto byTimeSeries = stockInfoFetcher.getByTimeSeries(ticker);
        List<CoinCandleDto.Series> series = byTimeSeries.getSeries();
        return StockService.super.getAllByAfterBeginDate(ticker, beginDt);
    }

    @Override
    public List<E> FetchToDB() {
        List<E> all = (List<E>) coinRepository.findAll();

        if( !all.isEmpty() ){
            return all;
        }

        for (Map.Entry<String, CoinAllInfoDto.CoinDetail> entry : stockInfoFetcher.getAll().getCoinDetails().entrySet()){
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
    public Optional<E> findStockByStockCode(String code) {
        return (Optional<E>) Optional.ofNullable(coinRepository.findByCode(code).orElseThrow(() -> new ResourceCommException("Coin이 존재하지 않습니다.")));
    }

    @Override
    public List<E> getStocksByStockLike(String keyword) {
        return (List<E>) coinRepository.findByCodeLike(keyword);
    }
}
