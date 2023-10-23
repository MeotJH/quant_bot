package com.server.quant_bot.korea.service;

import lombok.RequiredArgsConstructor;
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

    @Override
    public ResponseEntity<Map> get(String ticker) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo";
        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("serviceKey", "mHqx2h2fD1G51%2B4T7ehY3PIhNURT5sijsIul77qBmBNDYnL0cDNN0DRvFdSMN7eJzFszqknTLM3L2vlgAl7Vjg%3D%3D")
                .queryParam("resultType", "json")
                .queryParam("itmsNm", "%EC%82%BC%EC%84%B1%EC%A0%84%EC%9E%90")
                .build(true).encode().toUri();


        String apiUrl = "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo?serviceKey=mHqx2h2fD1G51%2B4T7ehY3PIhNURT5sijsIul77qBmBNDYnL0cDNN0DRvFdSMN7eJzFszqknTLM3L2vlgAl7Vjg%3D%3D&resultType=json&itmsNm=%EC%82%BC%EC%84%B1%EC%A0%84%EC%9E%90";

        ResponseEntity<Map> exchange = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
        return exchange;
    }
}