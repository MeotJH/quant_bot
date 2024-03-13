package com.server.quant_bot.stock.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.quant_bot.stock.dto.CoinDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoinInfoFetcher implements StockInfoFetcher{

    private final RestTemplate restTemplate;

    @Value("${finance.coin-url}")
    private String SERVICE_URL;

    private final String END_POINT = "/public/ticker/ALL_";

    @Override
    public CoinDto getAll() {
        CoinDto response = restTemplate.getForObject(
                getUrlDefaultBuilder("KRW").build(true).toUri()
                , CoinDto.class);
        pasrResponse(response);
        return response;
    }

    private CoinDto pasrResponse(CoinDto response) {
        Map<String, Object> data = response.getData();
        String date = data.get("date").toString();
        data.remove("date");
        ObjectMapper om = new ObjectMapper();


        Map<String, CoinDto.CoinDetail> coinDetails = new HashMap<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                CoinDto.CoinDetail coinDetail = om.convertValue(value, CoinDto.CoinDetail.class);
                coinDetails.put(key, coinDetail);
            }
        }
        response.setDate(date);
        response.setCoinDetails(coinDetails);
        log.info(response.getDate());
        return response;
    }

    private UriComponentsBuilder getUrlDefaultBuilder(String currency) {
        return UriComponentsBuilder.fromHttpUrl(SERVICE_URL + END_POINT + currency)
                .queryParam("resultType", "json");
    }
}
