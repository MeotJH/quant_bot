package com.server.quant_bot.news.service;

import com.server.quant_bot.news.dto.NewsDto;
import org.springframework.stereotype.Service;

@Service
public interface NewsService {

    default NewsDto get(int size){return null;}
}
