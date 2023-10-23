package com.server.quant_bot.korea.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.quant_bot.korea.dto.PublicDataStockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PublicDataStockService implements StockService{

    @Value("${finance.key}")
    private String SERVICE_KEY;

    @Value("${finance.url}")
    private String SERVICE_URL;

    private final ObjectMapper om;

    @Override
    public List<PublicDataStockDto>  get(String ticker) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String encodedTicker = getEncode(ticker);

        URI uri = UriComponentsBuilder.fromHttpUrl(SERVICE_URL)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("resultType", "json")
                .queryParam("itmsNm", encodedTicker)
                .build(true).toUri();


        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        return getDtos(response);
    }

    private String getEncode(String ticker) {
        return URLEncoder.encode(ticker, StandardCharsets.UTF_8);
    }

    private List<PublicDataStockDto> getDtos(ResponseEntity<String> response) {
        List<PublicDataStockDto> dtos = new ArrayList<>();
        try {
            JsonNode root = om.readTree(response.getBody());
            root
                    .path("response")
                    .path("body")
                    .path("items")
                    .path("item")
                    .elements()
                    .forEachRemaining( each -> {

                        try {
                            dtos.add(om.treeToValue(each, PublicDataStockDto.class));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                    });

            return dtos;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}