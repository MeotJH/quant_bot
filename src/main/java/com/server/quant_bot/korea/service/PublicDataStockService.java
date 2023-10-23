package com.server.quant_bot.korea.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PublicDataStockService implements StockService{

    @Value("${finance.key}")
    private String SERVICE_KEY;

    @Override
    public ResponseEntity<Map> get(String ticker) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo";
        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("resultType", "json")
                .queryParam("itmsNm", "%EC%82%BC%EC%84%B1%EC%A0%84%EC%9E%90")
                .build(true).encode().toUri();




        ResponseEntity<Map> exchange = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
        return exchange;
    }
}