package com.server.quant_bot.stock.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.quant_bot.stock.dto.CoinDto;
import com.server.quant_bot.stock.mapper.CoinMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="TB_COIN")
public class Coin {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Comment("코인명")
    @Column
    private String name;

    @Comment("코인코드")
    @Column
    private String code;

    @Comment("시가 00시 기준")
    @Column
    private String openingPrice;

    @Comment("종가 00시 기준")
    @Column
    private String closingPrice;

    @Comment("저가 00시 기준")
    @Column
    private String minPrice;

    @Comment("고가 00시 기준")
    @Column
    private String maxPrice;

    @Comment("거래량 00시 기준")
    @Column
    private String unitsTraded;

    @Comment("거래금액 00시 기준")
    @Column
    private String accTradeValue;

    @Comment("전일종가")
    @Column
    private String prevClosingPrice;

    @Comment("최근 24시간 거래량")
    @Column
    private String unitsTraded24H;

    @Comment("최근 24시간 거래금액")
    @Column
    private String accTradeValue24H;

    @Comment("최근 24시간 변동가")
    @Column
    private String fluctate24H;

    @Comment("최근 24시간 변동률")
    @Column
    private String fluctateRate24H;

    public Coin toEntity(CoinDto.CoinDetail dto){
        return CoinMapper.INSTANCE.DtoToEntity(dto);
    }
}
