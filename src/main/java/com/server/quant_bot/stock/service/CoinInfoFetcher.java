package com.server.quant_bot.stock.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.quant_bot.stock.dto.CoinAllInfoDto;
import com.server.quant_bot.stock.dto.CoinCandleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoinInfoFetcher implements StockInfoFetcher{

    private final RestTemplate restTemplate;

    @Value("${finance.coin-url}")
    private String SERVICE_URL;

    private final String END_POINT = "/public/ticker/ALL_";

    private final String CANDLE_END_POINT = "/public/candlestick";

    @Override
    public CoinAllInfoDto getAll() {
        CoinAllInfoDto response = restTemplate.getForObject(
                getUrlDefaultBuilder(END_POINT+"KRW").build(true).toUri()
                , CoinAllInfoDto.class);
        return pasrResponse(response);
    }

    @Override
    public CoinCandleDto getByTimeSeries(String ticker) {
        CoinCandleDto response = restTemplate.getForObject(
                getUrlDefaultBuilder(CANDLE_END_POINT + "/" +ticker + "_KRW/24h").build(true).toUri()
                , CoinCandleDto.class);
        return parseResponse(response);
    }

    private CoinAllInfoDto pasrResponse(CoinAllInfoDto response) {
        Map<String, Object> data = response.getData();
        String date = data.get("date").toString();
        data.remove("date");
        ObjectMapper om = new ObjectMapper();


        Map<String, CoinAllInfoDto.CoinDetail> coinDetails = new HashMap<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                CoinAllInfoDto.CoinDetail coinDetail = om.convertValue(value, CoinAllInfoDto.CoinDetail.class);
                coinDetail.setCode(entry.getKey());
                coinDetails.put(key, coinDetail);
            }
        }
        response.setDate(date);
        response.setCoinDetails(coinDetails);
        log.info(response.getDate());
        return response;
    }

    private CoinCandleDto parseResponse(CoinCandleDto response){
        List<List> data = response.getData();

        List<CoinCandleDto.Series> series= new ArrayList<>();
        for (int i = data.size()-1; i >= 0; i--) {
            series.add(
                    new CoinCandleDto.Series()
                            .toDto( data.get(i) )
            );
        }
        response.setSeries(series);
        return response;
    }

    private UriComponentsBuilder getUrlDefaultBuilder(String endPoint) {
        return UriComponentsBuilder.fromHttpUrl(SERVICE_URL + endPoint)
                .queryParam("resultType", "json");
    }
}
