package com.server.quant_bot.stock.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.quant_bot.stock.dto.CoinAllInfoDto;
import com.server.quant_bot.stock.dto.CoinCandleDto;
import com.server.quant_bot.stock.dto.UpbitMarketCoinsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoinInfoFetcher implements StockInfoFetcher{

    private final RestTemplate restTemplate;

    @Value("${finance.coin-url}")
    private String SERVICE_URL;


    /**
     * return ex) [{"market":"KRW-BTC","korean_name":"비트코인","english_name":"Bitcoin"},....]
     */
    private String SERVICE_URL_UPBIT = "https://api.upbit.com/v1/market/all";

    private final String END_POINT = "/public/ticker/ALL_";

    private final String CANDLE_END_POINT = "/public/candlestick";

    @Override
    public CoinAllInfoDto getAll() {
        CoinAllInfoDto response = restTemplate.getForObject(
                getEndPoint(SERVICE_URL + END_POINT + "KRW")
                , CoinAllInfoDto.class);

        List<UpbitMarketCoinsDto> upbitMarketCoinsDtos = restTemplate.getForObject(
                getEndPoint(SERVICE_URL_UPBIT)
                , List.class);

        return parseResponse(response, upbitMarketCoinsDtos);
    }

    private URI getEndPoint(String url){
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("resultType", "json").build(true).toUri();
    }

    @Override
    public CoinCandleDto getByTimeSeries(String ticker) {
        CoinCandleDto response = restTemplate.getForObject(
                getUrlDefaultBuilder(CANDLE_END_POINT + "/" +ticker + "_KRW/24h").build(true).toUri()
                , CoinCandleDto.class);
        return parseResponse(response);
    }

    private CoinAllInfoDto parseResponse(CoinAllInfoDto response, List<UpbitMarketCoinsDto> upbitMarketCoinsDtos) {
        Map<String, Object> data = response.getData();
        String date = data.get("date").toString();
        data.remove("date");
        ObjectMapper om = new ObjectMapper();

        Map<String, UpbitMarketCoinsDto> coinNameMap = new HashMap<>();
        for (UpbitMarketCoinsDto each : upbitMarketCoinsDtos){
            String ticker = each.getMarket().split("-")[1];
            coinNameMap.put(ticker,each);
        }


        Map<String, CoinAllInfoDto.CoinDetail> coinDetails = new HashMap<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            UpbitMarketCoinsDto nameDto = coinNameMap.get(key);

            if (value instanceof Map) {
                CoinAllInfoDto.CoinDetail coinDetail = om.convertValue(value, CoinAllInfoDto.CoinDetail.class);
                coinDetail.setCode(key);
                coinDetail.setStockName(nameDto.getStockNames());
                coinDetail.setStockEngName(nameDto.getStockEngName());
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
