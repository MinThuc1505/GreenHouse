package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenhouse.model.PriceHistory;

public interface PriceHistoryDAO extends JpaRepository<PriceHistory, Integer> {

}
