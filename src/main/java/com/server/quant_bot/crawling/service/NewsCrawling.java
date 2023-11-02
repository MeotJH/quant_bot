package com.server.quant_bot.crawling.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NewsCrawling implements Crawling{

    @Value("${finance.news.url}")
    private String FINANCE_NEWS_URL;

    List<Map> news = new ArrayList<>();



    public List<Map> get(){

        Document doc = null;
        try {
            doc = Jsoup.connect(FINANCE_NEWS_URL).get();
        } catch (Exception e) {
            throw new RuntimeException("크롤링 에서 에러가 발생했다!");
        }
        //재귀함수로 리팩
        Elements elements = doc.select("ul.news-list");
        findHrefNodeInNode(elements);
        return this.news;

    }

    private void findHrefNodeInNode(Elements elements) {

        //Elements내부 Element안에 자식이 Elements면 재귀함수 호출해
        if( elements instanceof Elements){
            for (Element each: elements) {

                //a태그안에 href이고 그 태그 text가 " " 아니면 넣어줘
                if(each.hasAttr("href") && !each.childNodes().get(0).toString().equals(" ")){
                        Map<String,String> newsOne = new HashMap<>();
                        newsOne.put("href",each.attr("href"));
                        newsOne.put("title",each.childNodes().get(0).toString());
                        news.add(newsOne);

                }

                findHrefNodeInNode(each.children());

            }
        }
    }
}
