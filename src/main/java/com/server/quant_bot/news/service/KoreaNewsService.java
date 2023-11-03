package com.server.quant_bot.news.service;

import com.server.quant_bot.news.dto.NewsDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class KoreaNewsService implements NewsService {

    @Value("${finance.news.url}")
    private String FINANCE_NEWS_URL;

    List<Map> news = new ArrayList<>();



    @Override
    public NewsDto get(int size){
        Document doc = null;
        try {
            doc = Jsoup.connect(FINANCE_NEWS_URL).get();
        } catch (Exception e) {
            throw new RuntimeException("크롤링 에서 에러가 발생했다!");
        }
        //재귀함수로 리팩
        Elements elements = doc.select("ul.news-list");
        findHrefNodeInNode(elements,size);

        List<Map> news = cleanNews();


        return new NewsDto(news);

    }


    private void findHrefNodeInNode(Elements elements, int size) {

        //Elements내부 Element안에 자식이 Elements면 재귀함수 호출해
        if( elements instanceof Elements){
            for (Element each: elements) {

                //a태그안에 href이고 그 태그 text가 " " 아니면 넣어줘
                if(each.hasAttr("href") ){
                    each.childNodes().stream().iterator().forEachRemaining( node -> {
                        if(node.hasAttr("src")){
                            if(news.size() < size){
                                log.info("Each.toString ::::::: {}",each.toString());
                                setNews(each, node);
                            }


                        }
                    });
                }
                findHrefNodeInNode(each.children(),size);

            }
        }
    }

    private List<Map> cleanNews() {
        List<Map> news = this.news;
        this.news = new ArrayList<>();
        return news;
    }

    private void setNews(Element each, Node node) {
        Map<String,String> newsOne = new HashMap<>();
        String src = node.attr("src");
        String alt = node.attr("alt");
        newsOne.put("href", each.attr("href"));
        newsOne.put("src",src);
        newsOne.put("alt",alt);
        news.add(newsOne);
    }
}
