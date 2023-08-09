package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenhouse.model.BillDetail;

public interface BillDetailDAO extends JpaRepository<BillDetail, Integer> {
    
}
