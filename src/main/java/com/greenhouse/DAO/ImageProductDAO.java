package com.greenhouse.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenhouse.model.ImageProduct;


public interface ImageProductDAO extends JpaRepository<ImageProduct, Integer> {

    @Query(value = "select * from Image_Product where Product_Id = ?1", nativeQuery = true)
    List<ImageProduct> getImageByProductId(String productId);
 
}
