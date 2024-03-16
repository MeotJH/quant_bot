package com.server.quant_bot.stock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.quant_bot.comm.exception.ResourceCommException;
import com.server.quant_bot.stock.dto.PublicDataStockDto;
import com.server.quant_bot.stock.dto.StockDto;
import com.server.quant_bot.stock.entity.Stock;
import com.server.quant_bot.stock.mapping.StockMapping;
import com.server.quant_bot.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Primary
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

    //TODO 이거 지금 name으로 검색하는것 같은데 code로 새로운 임플 만들기
    @Override
    public List<StockDto>  get(String ticker) {
        ResponseEntity<String> response = restTemplate.exchange(
                getUrlDefaultBuilder(ticker).build(true).toUri()
                , HttpMethod.GET
                , getHttpEntity()
                , String.class);

        return getDtos(response);
    }

    @Override
    public List<StockDto>  getAllByAfterBeginDate(String ticker, String beginDt) {

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
    public List<Stock> FetchToDB() throws IOException {

        if(stockRepository.findAll().size() > 0){
            return new ArrayList<>();
        }

        // Load the resource
        String line;
        String csvSplitBy = ",";
        List<Stock> stocks = new ArrayList<>();
        Resource resource = new ClassPathResource(CSV_PATH);
        try (BufferedReader br = new BufferedReader(new FileReader( resource.getFile())) ) {

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
    public Optional<Stock> findStockByStockCode(String stockCode) {
        return stockRepository.findByStockCode(stockCode);
    }

    @Override
    public List<StockMapping> getStocksByStockLike(String keyword) {
        return stockRepository.findStocksByStockNameLike("%"+keyword+"%");
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

    private List<StockDto> getDtos(ResponseEntity<String> response) {
        List<StockDto> dtos = new ArrayList<>();
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
                            PublicDataStockDto publicDataStockDto = om.treeToValue(each, PublicDataStockDto.class);
                            dtos.add(
                                    StockDto.builder()
                                            .closingPrice(
                                                    Double.valueOf(  publicDataStockDto.getClpr()  )
                                            )
                                            .baseDate( publicDataStockDto.getBasDt() )
                                            .build()
                            );
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

    private void defineDataExist(List<StockDto> dtos) {

        if(dtos.isEmpty()){
            throw new ResourceCommException("결과값이 없습니다.");
        }
    }


}