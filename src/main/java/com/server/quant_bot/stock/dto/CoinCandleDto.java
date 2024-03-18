package com.server.quant_bot.stock.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CoinCandleDto {

    private String status;
    private List<List> data = new ArrayList<>();
    private List<CoinCandleDto.Series> series;

    @Getter
    @ToString
    public static class Series {

        private Long time;
        private String openingPrice;
        private String closingPrice;
        private String maxPrice;
        private String minPrice;
        private String tradingVolume;

        public Series toDtoFromList(List list){
            int i = 0;
            this.time = (Long) list.get(i++);
            this.openingPrice = (String) list.get(i++);
            this.closingPrice = (String) list.get(i++);
            this.maxPrice = (String) list.get(i++);
            this.minPrice = (String) list.get(i++);
            this.tradingVolume = (String) list.get(i);

            return this;
        }

    }
}
