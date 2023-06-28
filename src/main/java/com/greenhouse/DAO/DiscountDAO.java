package com.greenhouse.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenhouse.model.Discount;

public interface DiscountDAO extends JpaRepository<Discount, Integer> {
    @Query("SELECT d FROM Discount d WHERE "
            + "d.discountCode LIKE %?1% "
            + "OR d.discountPercent < ?2 ")
    List<Discount> findByKeyword(String discountCode, Float discountPercent);
}
