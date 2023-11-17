package com.server.quant_bot.korea.entity;

import com.server.quant_bot.comm.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@Table(name="TB_STOCKS")
public class Stock extends BaseEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String stockCode;

    @Column
    private String stockName;

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
