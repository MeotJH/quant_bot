package com.server.quant_bot.quant.trend_following.repository;

import com.server.quant_bot.comm.security.entity.UserEntity;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrendFollowRepository extends JpaRepository<TrendFollow, UUID>{
    List<TrendFollow> findAllByUser(UserEntity user);

}
