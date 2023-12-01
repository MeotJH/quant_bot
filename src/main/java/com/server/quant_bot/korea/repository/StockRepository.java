package com.server.quant_bot.korea.repository;

import com.server.quant_bot.korea.entity.Stock;
import com.server.quant_bot.korea.mapping.StockMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByStockName(String stockCode);

    List<StockMapping> findStocksByStockNameLike(String keyword);
}
