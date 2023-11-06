package com.server.quant_bot.comm.initial;

import com.server.quant_bot.korea.service.StockService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitData {

    private final StockService stockService;

    /**
     * 스프링 시작될때 넣어야 하는 기준데이터
     */
    @PostConstruct
    private void init(){
        //코스피 및 코스닥 정보 init
        stockService.CSVToDB();
    }

}
