package com.server.quant_bot.crawling.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NewsCrawling implements Crawling{

    List<Map> news = new ArrayList<>();

    public List<Map> get(){
        // 크롤링 할 사이트 주소.
        String url = "https://www.hankyung.com/finance";

        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();
        } catch (Exception e) {
            System.out.println(e);
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
