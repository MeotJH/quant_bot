package com.server.quant_bot.comm.initial;

import com.server.quant_bot.comm.security.service.UserService;
import com.server.quant_bot.korea.service.StockService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitData {

    private final StockService stockService;
    private final UserService userService;

    /**
     * 스프링 시작될때 넣어야 하는 기준데이터
     */
    @PostConstruct
    private void init(){
        //TODO jpa 설정 -> auto면 실행시키기
        //코스피 및 코스닥 정보 init
        //stockService.CSVToDB();

        //유저 1명 세팅 TODO ADMIN으로 권한주기
        //userService.initUser();
    }

}
