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

    @Query(value = " select stock_code as stockName , stock_code as stockCode from tb_coin where stock_code like :code ", nativeQuery = true)
    List<StockMapping> findByStockCodeLike(@Param("code") String code);

}
