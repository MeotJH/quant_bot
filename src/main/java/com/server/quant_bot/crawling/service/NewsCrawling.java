package com.server.quant_bot.crawling.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class NewsCrawling implements Crawling{

    public int get(){
        // 크롤링 할 사이트 주소.
        String url = "https://www.hankyung.com/finance";

        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();
        } catch (Exception e) {
            System.out.println(e);
        }

        Elements element = doc.select("ul.news-list");
        return element.size();

    }
}
