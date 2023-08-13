package com.greenhouse.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenhouse.model.Discount;

public interface DiscountDAO extends JpaRepository<Discount, String> {
 
}
