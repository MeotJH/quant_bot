package com.server.quant_bot.comm.initial;

import com.server.quant_bot.comm.security.service.UserService;
import com.server.quant_bot.stock.entity.Coin;
import com.server.quant_bot.stock.enums.StockType;
import com.server.quant_bot.stock.service.StockService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class InitData {

    private final Map<String, StockService> stockServices;
    private final UserService userService;

    /**
     * 스프링 시작될때 넣어야 하는 기준데이터
     */
    @PostConstruct
    private void init() throws IOException {
        //코스피 및 코스닥 정보 init
        stockServices.get(StockType.KOREA_STOCK.STOCK_SERVICE).FetchToDB();
        //유저 1명 세팅 TODO ADMIN으로 권한주기
        userService.initUser();
        //코인 정보 init
        stockServices.get(StockType.COIN.STOCK_SERVICE).FetchToDB();
    }

}
