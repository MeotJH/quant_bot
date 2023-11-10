package com.server.quant_bot.korea.repository;

import com.server.quant_bot.korea.entity.Stock;
import com.server.quant_bot.korea.mapping.StockMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    List<StockMapping> findStocksByStockNameLike(String keyword);
}
