package com.server.quant_bot.quant.enums;

import lombok.Getter;

@Getter
public enum QuantType {

    TREND_FOLLOW("추세평균투자");

    private String korean;

    QuantType(String korean){
        this.korean = korean;
    }
}
