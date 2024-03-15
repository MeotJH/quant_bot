package com.server.quant_bot.stock.repository;


import com.server.quant_bot.stock.entity.Coin;
import com.server.quant_bot.stock.mapping.StockMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CoinRepository extends JpaRepository<Coin, UUID> {

    Optional<Coin> findByStockCode(String code);
    Optional<Coin> findByStockName(String stockName);
    List<StockMapping> findByStockNameLike(@Param("code") String code);

}
