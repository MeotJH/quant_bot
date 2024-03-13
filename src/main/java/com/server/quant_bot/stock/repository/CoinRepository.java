package com.server.quant_bot.stock.repository;


import com.server.quant_bot.stock.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CoinRepository extends JpaRepository<Coin, UUID> {

    Optional<Coin> findByCode(String code);

    List<Coin> findByCodeLike(String code);
}
