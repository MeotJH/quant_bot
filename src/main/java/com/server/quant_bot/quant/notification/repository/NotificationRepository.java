package com.server.quant_bot.quant.notification.repository;

import com.server.quant_bot.quant.notification.entity.Notification;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {


}
