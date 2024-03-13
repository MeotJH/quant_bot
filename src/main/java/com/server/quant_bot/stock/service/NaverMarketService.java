package com.server.quant_bot.stock.service;

import com.server.quant_bot.stock.dto.MarketDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NaverMarketService implements MarketService{

    @Value("${finance.market.url}")
    private String MARKET_URL;

    private StringBuffer marketDataStr = new StringBuffer();

    @Override
    public MarketDto getData() {
        Document doc = null;

        try {
            doc = Jsoup.connect(MARKET_URL).get();
        } catch (Exception e) {
            throw new RuntimeException("크롤링 에서 에러가 발생했다!");
        }
        //재귀함수로 리팩
        Elements marketData = doc.select("span.spt_con");
        Elements marketImg = doc.select("span.img_bx");

        List<String> marketDatas = new ArrayList<>();
        for (Element each: marketData) {
            getTextNodeToStr(each);
            marketDatas.add(marketDataStr.toString());
            marketDataStr = new StringBuffer();
        }

        List<String> marketImages = parseImg(marketImg);
        Map<String,List> marketResult = new HashMap<>();
        marketResult.put("data",marketDatas);
        marketResult.put("img",marketImages);
        return new MarketDto(marketResult);
    }

    private List<String> parseImg(Elements marketImg) {
        List<String> marketImgStr = new ArrayList<>();
        marketImg.forEach( each -> {
            Elements imgTag = each.select("img");
            marketImgStr.add(imgTag.attr("src"));
        });
        return marketImgStr;
    }

    private void getTextNodeToStr(Element element) {

        for (int i = 0; i < element.children().size(); i++) {
            Element child = element.child(i);
            if(child.hasText()){
                marketDataStr.append(child.text() + " ");
            }
            getTextNodeToStr(child);
        }
    }
}
