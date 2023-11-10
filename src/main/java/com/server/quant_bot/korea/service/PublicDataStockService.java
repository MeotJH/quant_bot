package com.server.quant_bot.korea.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.quant_bot.comm.exception.ResourceNotFoundException;
import com.server.quant_bot.korea.dto.PublicDataStockDto;
import com.server.quant_bot.korea.entity.Stock;
import com.server.quant_bot.korea.mapping.StockMapping;
import com.server.quant_bot.korea.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
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

    @Value("${finance.csv.path}")
    private String CSV_PATH;

    private final ObjectMapper om;
    private final RestTemplate restTemplate;
    private final StockRepository stockRepository;

    @Override
    public List<PublicDataStockDto>  get(String ticker) {
        ResponseEntity<String> response = restTemplate.exchange(
                getUrlDefaultBuilder(ticker).build(true).toUri()
                , HttpMethod.GET
                , getHttpEntity()
                , String.class);

        return getDtos(response);
    }

    @Override
    public List<PublicDataStockDto>  getAllByAfterBeginDate(String ticker, String beginDt) {

        URI uri = getUrlDefaultBuilder(ticker)
                .queryParam("numOfRows", "1000")
                .queryParam("beginBasDt", beginDt)
                .build(true)
                .toUri();

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, getHttpEntity(), String.class);
        return getDtos(response);
    }

    @Override
    @Transactional( rollbackFor = Exception.class)
    public List<Stock> CSVToDB() {
        String line;
        String csvSplitBy = ",";
        List<Stock> stocks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH)) ) {

            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                Stock stock = new Stock();
                stock.updateCsv(data);
                Stock save = stockRepository.save(stock);
                stocks.add(save);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stocks;
    }

    @Override
    public List<StockMapping> getStocksByStockLike(String keyword) {
        return stockRepository.findStocksByStockNameLike(keyword);
    }

    private HttpEntity<?> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    private String getEncode(String ticker) {
        return URLEncoder.encode(ticker, StandardCharsets.UTF_8);
    }


    private UriComponentsBuilder getUrlDefaultBuilder(String ticker) {
        return UriComponentsBuilder.fromHttpUrl(SERVICE_URL)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("resultType", "json")
                .queryParam("itmsNm", getEncode(ticker));
    }

    private UriComponentsBuilder getUrlNameLikeBuilder(String ticker) {
        return UriComponentsBuilder.fromHttpUrl(SERVICE_URL)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("resultType", "json")
                .queryParam("likeItmsNm", getEncode(ticker));
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
            defineDataExist(dtos);
            return dtos;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void defineDataExist(List<PublicDataStockDto> dtos) {

        if(dtos.isEmpty()){
            throw new ResourceNotFoundException("결과값이 없습니다.");
        }
    }

}