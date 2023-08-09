package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenhouse.model.ImportProduct;

public interface ImportProductDAO extends JpaRepository<ImportProduct, Integer> {

}
