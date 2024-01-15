package com.server.quant_bot.quant.notification.entity;

import com.server.quant_bot.comm.entity.BaseEntity;
import com.server.quant_bot.korea.entity.Stock;
import com.server.quant_bot.quant.notification.dto.NotificationMapperDto;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.util.UUID;

@Getter
@Entity
public class Notification extends BaseEntity {

    @Id
    @Column(name = "NOTIFICATION_ID")
    @Comment("알림PK")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Comment("알림 수신 YN")
    private Boolean approval;

    @Comment("알림 상태")
    @ColumnDefault("false")
    private Boolean status;

    @OneToOne(mappedBy = "notification")
    @Comment("추세투자")
    private TrendFollow trendFollow;

    //TODO 카톡알림같은거 여기다가 필요 컬럼 넣아야 한다.

    @Override
    public BaseEntity update(Object beforeDto) {
        NotificationMapperDto dto = (NotificationMapperDto) beforeDto;
        approval = dto.approval();
        trendFollow = dto.trendFollow();
        status = false;
        return this;
    }

    public Notification addStatus(Boolean status){
        this.status = status;
        return this;
    }
}
