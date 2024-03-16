package com.server.quant_bot.stock.repository;

import com.server.quant_bot.stock.entity.Stock;
import com.server.quant_bot.stock.mapping.StockMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {

    List<Stock> findByMarketIn(String[] markets);

    Optional<Stock> findByStockCode(String stockCode);

    List<StockMapping> findStocksByStockNameLike(String keyword);
}
