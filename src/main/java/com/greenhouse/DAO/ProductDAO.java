package com.greenhouse.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenhouse.model.Product;

public interface ProductDAO extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT * FROM Products WHERE Products.Id IN (SELECT TOP 12 p.Id FROM Products p JOIN Bill_Detail bd ON p.Id = bd.Product_Id JOIN Bills b ON b.Id = bd.Bill_Id WHERE b.Status = '1' GROUP BY p.Id, p.Name ORDER BY SUM(bd.Quantity) DESC)", nativeQuery = true)
    List<Product> findTop12ProductsBestSale();

    @Query(value = "select * from Products where Id in ( select Product_Id from Set_Category sc  where sc.Category_Id = ?1) and Quantity > 0", nativeQuery = true)
    List<Product> findByCategory(String category);

    @Query(value = "select * from Products where Id in ( select Product_Id from Set_Category sc  where sc.Category_Id = ?1) and Quantity > 0 AND Price <= ?2", nativeQuery = true)
    List<Product> findByCategoryAndPrice(String category, String price);

    @Query(value = "select * from Products where Quantity > 0 AND Price <= ?1", nativeQuery = true)
    List<Product> findByPrice(String price);

    @Query(value = "select p.* from Products p JOIN Set_Category sc ON p.Id = sc.Product_Id where sc.Category_Id = ?1", nativeQuery = true)
    List<Product> getProductsOfSameType(String category);

    boolean existsByName(String name);
}
