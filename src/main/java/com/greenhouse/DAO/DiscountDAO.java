package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenhouse.model.Discount;

public interface DiscountDAO extends JpaRepository<Discount, String> {
   
}
