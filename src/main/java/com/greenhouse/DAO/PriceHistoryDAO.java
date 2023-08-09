package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenhouse.model.PriceHistory;
import com.greenhouse.model.Product;

import jakarta.transaction.Transactional;

public interface PriceHistoryDAO extends JpaRepository<PriceHistory, Integer> {
    @Transactional
  void deleteByProduct(Product product);
}
