package com.server.quant_bot.crawling.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface Crawling {

    default List<Map> get(){return null;}
}
