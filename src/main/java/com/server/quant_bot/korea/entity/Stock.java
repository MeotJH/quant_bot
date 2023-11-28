package com.server.quant_bot.korea.entity;

import com.server.quant_bot.comm.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

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



    public void updateCsv(String[] dto){
        this.stockCode = dto[0];
        this.stockName = dto[1];
        this.market = dto[2];
    }

    @Override
    public BaseEntity update(Object dto) {
        return null;
    }
}
