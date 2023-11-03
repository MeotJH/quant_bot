package com.server.quant_bot.news.controller;

import com.server.quant_bot.news.dto.NewsDto;
import com.server.quant_bot.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService crawling;

    @GetMapping("/{size}")
    public NewsDto findStockByTicker(@PathVariable int size){
        return crawling.get(size);
    }
}
