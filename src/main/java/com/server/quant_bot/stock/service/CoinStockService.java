package com.server.quant_bot.stock.service;

import com.server.quant_bot.comm.exception.ResourceCommException;
import com.server.quant_bot.comm.utill.DateUtill;
import com.server.quant_bot.stock.dto.CoinAllInfoDto;
import com.server.quant_bot.stock.dto.CoinCandleDto;
import com.server.quant_bot.stock.dto.StockDto;
import com.server.quant_bot.stock.entity.Coin;
import com.server.quant_bot.stock.entity.Stock;
import com.server.quant_bot.stock.repository.CoinRepository;
import com.server.quant_bot.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CoinStockService<E> implements StockService{

    private final StockInfoFetcher stockInfoFetcher;
    private final CoinRepository coinRepository;
    private final StockRepository stockRepository;

    @Value("${finance.market.crypto}")
    private final String[] MARKETS;

    @Override
    public List<StockDto> get(String ticker) {
        return StockService.super.get(ticker);
    }

    @Override
    public List<StockDto> getAllByAfterBeginDate(String ticker, String beginDt) {
        CoinCandleDto byTimeSeries = stockInfoFetcher.getByTimeSeries(ticker);
        List<CoinCandleDto.Series> series = byTimeSeries.getSeries();

        List<StockDto> dtos = new ArrayList<>();
        for(CoinCandleDto.Series each : series){

            String baseDate = each.getTime().toString();
            StockDto dto = StockDto
                    .builder()
                    .closingPrice(Double.valueOf(each.getClosingPrice()))
                    .baseDate(baseDate)
                    .build();
            dtos.add(dto);
        }
        return dtos;
    }


    @Override
    public List<E> FetchToDB() {
        /**
         * TODO Stock 리팩토링중
         *  stock , coin이 나누어 있는데
         *  coin 테이블의 code, stockName, stockEngName 없애고
         *  stock 부모 coin 자식으로 만들기
         *  지금은 stock랑 coin이 연관이 없음
         */
        List<E> all = (List<E>) coinRepository.findAll();

        if( isCoinsNotEmpty() ){
            return all;
        }

        for (Map.Entry<String, CoinAllInfoDto.CoinDetail> entry : stockInfoFetcher.getAll().getCoinDetails().entrySet()){
            // coins에 값 넣어주기
            all.add(
                    (E) coinRepository.save(
                            new Coin().toEntity(
                                    entry.getValue()
                            )
                    )
            );
            
            // stock에 암호화폐값 넣기
            stockRepository.save(
                    new Stock().update(
                            entry.getValue()
                            ,MARKETS[0]
                    )
            );
        }

        return all;
    }

    @Override
    public Optional<E> findStockByStockCode(String code) {
        return (Optional<E>) Optional.ofNullable(coinRepository.findByStockCode(code).orElseThrow(() -> new ResourceCommException("Coin이 존재하지 않습니다.")));
    }

    @Override
    public List<E> getStocksByStockLike(String keyword) {
        return (List<E>) coinRepository.findByStockNameLike("%"+keyword+"%");
    }

    private boolean isCoinsNotEmpty() {
        return (!coinRepository.findAll().isEmpty()) && !(stockRepository.findByMarketIn(MARKETS).size() == 0);
    }

}
