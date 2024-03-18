package com.server.quant_bot.stock.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.quant_bot.comm.exception.ResourceCommException;
import com.server.quant_bot.stock.dto.CoinAllInfoDto;
import com.server.quant_bot.stock.dto.CoinCandleDto;
import com.server.quant_bot.stock.dto.UpbitMarketCoinsDto;
import com.server.quant_bot.stock.entity.Coin;
import com.server.quant_bot.stock.repository.CoinRepository;
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
    private final CoinRepository coinRepository;

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

        List<Map<String,String>> upbitMarketCoinsDtos = restTemplate.getForObject(
                getEndPoint(SERVICE_URL_UPBIT)
                , List.class);

        return parseResponse(response, upbitMarketCoinsDtos);
    }

    private URI getEndPoint(String url){
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("resultType", "json").build(true).toUri();
    }

    @Override
    public CoinCandleDto getByTimeSeries(String stockName) {
        Optional<Coin> byStockName = coinRepository.findByStockName(stockName);
        String stockCode = byStockName.orElseThrow(() -> new ResourceCommException("해당하는 코인이 없습니다.")).getStockCode();

        CoinCandleDto response = restTemplate.getForObject(
                getUrlDefaultBuilder(CANDLE_END_POINT + "/" +stockCode + "_KRW/24h").build(true).toUri()
                , CoinCandleDto.class);
        return parseResponse(response);
    }

    private CoinAllInfoDto parseResponse(CoinAllInfoDto response, List<Map<String,String>> upbitMarketCoins) {
        Map<String, UpbitMarketCoinsDto> coinNameMap = getTickerNames(upbitMarketCoins);
        return getDto(response, coinNameMap);
    }

    private Map<String, UpbitMarketCoinsDto> getTickerNames(List<Map<String, String>> upbitMarketCoins) {
        Map<String, UpbitMarketCoinsDto> coinNameMap = new HashMap<>();
        for (Map<String,String> each : upbitMarketCoins){
            String market =  each.get("market");
            String ticker = market.split("-")[1];
            String koreanName = each.get("korean_name");
            String englishName = each.get("english_name");
            UpbitMarketCoinsDto dto = UpbitMarketCoinsDto
                    .builder()
                    .ticker(ticker)
                    .stockNames(koreanName)
                    .stockEngName(englishName)
                    .build();
            coinNameMap.put (ticker,dto );
        }
        return coinNameMap;
    }

    private CoinAllInfoDto getDto(CoinAllInfoDto response, Map<String, UpbitMarketCoinsDto> coinNameMap) {
        Map<String, Object> data = response.getData();
        String date = data.get("date").toString();
        data.remove("date");
        ObjectMapper om = new ObjectMapper();
        Map<String, CoinAllInfoDto.CoinDetail> coinDetails = new HashMap<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(!coinNameMap.containsKey(key)){
                continue;
            }
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
                            .toDtoFromList( data.get(i) )
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
