package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenhouse.model.Bill_Detail;

public interface BillDetailDAO extends JpaRepository<Bill_Detail, Integer> {
    
}
