package com.server.quant_bot.stock.entity;

import com.server.quant_bot.comm.entity.BaseEntity;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import com.server.quant_bot.stock.dto.CoinAllInfoDto;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name="TB_STOCKS")
public class Stock extends BaseEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Comment("주식코드")
    @Column
    private String stockCode;

    @Comment("주식명")
    @Column
    private String stockName;

    @Comment("포함된 시장")
    @Column
    private String market;

    @OneToMany(mappedBy = "stock")
    private List<TrendFollow> trendFollows = new ArrayList<TrendFollow>();

    public void updateCsv(String[] dto){
        this.stockCode = dto[0];
        this.stockName = dto[1];
        this.market = dto[2];
    }

    public Stock toEntity(CoinAllInfoDto.CoinDetail dto, String market){

        this.stockCode = dto.getCode();
        this.stockName = dto.getStockName();
        this.market = market;
        return this;
    }

    @Override
    public BaseEntity update(Object dto) {
        return null;
    }
}
