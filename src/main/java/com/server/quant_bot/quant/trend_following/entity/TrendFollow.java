package com.server.quant_bot.quant.trend_following.entity;

import com.server.quant_bot.comm.entity.BaseEntity;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.mapper.TrendFollowMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TrendFollow extends BaseEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(updatable = false, nullable = false)
    private String stock;

    private String trendFollowPrice;

    @Column(nullable = false)
    private Boolean isBuy;

    private String baseDateClosePrice;

    @Override
    public TrendFollow update(Object BeforeCastedDto) {
        TrendFollowDto dto = (TrendFollowDto) BeforeCastedDto;
        return TrendFollowMapper.INSTANCE.DtoToEntity(dto);
    }
}
