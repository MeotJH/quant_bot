package com.server.quant_bot.comm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    public abstract BaseEntity update(Object dto);

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime insertDate;

    @LastModifiedDate
    private LocalDateTime updateDate;
}
