package com.server.quant_bot.korea.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface StockService {

    default ResponseEntity<Map> get(String ticker){return null;}
}
