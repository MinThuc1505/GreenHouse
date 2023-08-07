package com.greenhouse.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenhouse.model.Product;

public interface ProductDAO extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM Products WHERE Products.Id IN ("
            + "SELECT TOP 10 p.Id "
            + "FROM Products p "
            + "JOIN Bill_Detail bd ON p.Id = bd.Product_Id "
            + "GROUP BY p.Id, p.Name "
            + "ORDER BY SUM(bd.Quantity) DESC)", nativeQuery = true)
    List<Product> findTop10ProductsBestSale();

    @Query(value = "select * from Products where Id in ( select Product_Id from SetCategory sc  where sc.Category_Id = ?1) and Quantity > 0", nativeQuery = true)
    List<Product> findByCategory(String category);

    @Query(value = "select * from Products where Id in ( select Product_Id from SetCategory sc  where sc.Category_Id = ?1) and Quantity > 0 AND Price <= ?2", nativeQuery = true)
    List<Product> findByCategoryAndPrice(String category, String price);

    @Query(value = "select * from Products where Quantity > 0 AND Price <= ?1", nativeQuery = true)
    List<Product> findByPrice(String price);
}
