package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenhouse.model.ImageProduct;


public interface ImageProductDAO extends JpaRepository<ImageProduct, Integer> {

}
