package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenhouse.model.BillImportProduct;


public interface BillImportProductDAO extends JpaRepository<BillImportProduct, Integer> {

}
