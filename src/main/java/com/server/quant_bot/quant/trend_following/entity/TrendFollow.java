package com.server.quant_bot.quant.trend_following.entity;

import com.server.quant_bot.comm.entity.BaseEntity;
import com.server.quant_bot.comm.security.entity.UserEntity;
import com.server.quant_bot.stock.entity.Stock;
import com.server.quant_bot.quant.notification.entity.Notification;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowEntityLikeDto;
import com.server.quant_bot.quant.trend_following.mapper.TrendFollowEnntityLikeMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("유저 ID")
    @JoinColumn(name="USER_ID" ,updatable = false, nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("주식코드")
    @JoinColumn(name = "STOCK_ID", nullable = false)
    private Stock stock;

    @OneToOne
    @Comment("알림여부")
    @JoinColumn(name = "NOTIFICATION_ID")
    private Notification notification;

    @Comment("추세이동선 검색시 값")
    private String trendFollowPrice;

    @Comment("매매여부")
    @Column(nullable = false)
    private Boolean isBuy;

    @Comment("검색시 시장 종결가")
    private String baseDateClosePrice;

    @Override
    public TrendFollow update(Object BeforeCastedDto) {
        TrendFollowEntityLikeDto dto = (TrendFollowEntityLikeDto) BeforeCastedDto;
        return TrendFollowEnntityLikeMapper.INSTANCE.DtoToEntity(dto);
    }

    public void addNoti(Notification notification){
        this.notification = notification;
    }


    public TrendFollowEntityLikeDto toDto(){
        return TrendFollowEnntityLikeMapper.INSTANCE.EntityToDto(this);
    }
}
