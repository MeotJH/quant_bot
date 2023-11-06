package com.server.quant_bot.korea.entity;

import com.server.quant_bot.comm.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="TB_STOCKS")
public class Stock extends BaseEntity {

    @Id
    @Column
    private String id;

    @Column
    private String stockCode;

    @Column
    private String stockName;

    @Column
    private String market;
}
