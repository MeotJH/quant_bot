package com.server.quant_bot.news.service;

import com.server.quant_bot.news.dto.NewsDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface Crawling {

    default NewsDto get(int size){return null;}
}
