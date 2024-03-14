package com.server.quant_bot.stock.service;

import com.server.quant_bot.comm.exception.ResourceCommException;
import com.server.quant_bot.comm.utill.DateUtill;
import com.server.quant_bot.stock.dto.CoinAllInfoDto;
import com.server.quant_bot.stock.dto.CoinCandleDto;
import com.server.quant_bot.stock.dto.StockDto;
import com.server.quant_bot.stock.entity.Coin;
import com.server.quant_bot.stock.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
public class CoinStockService<E> implements StockService{

    private final StockInfoFetcher stockInfoFetcher;
    private final CoinRepository coinRepository;

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

            String baseDate = DateUtill
                    .localDateTimeToString(
                            LocalDateTime.ofInstant(Instant.ofEpochSecond(each.getTime()), ZoneId.systemDefault())
                            , DateUtill.DEFAULT_DATE_TYPE
                    );
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
