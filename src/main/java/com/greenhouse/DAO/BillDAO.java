package com.greenhouse.DAO;

import com.greenhouse.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BillDAO extends JpaRepository<Bill, Integer> {

    @Query(value = "SELECT C.Id AS Category_Id, C.Name AS Category_Name, " +
            "SUM(BD.Quantity) AS Sold_Quantity, SUM(BD.Amount) AS Total_Sales, " +
            "(P.Quantity - COALESCE(SUM(BD.Quantity), 0)) AS Remaining_Quantity " +
            "FROM Categories C " +
            "INNER JOIN SetCategory SC ON C.Id = SC.Category_Id " +
            "INNER JOIN Products P ON SC.Product_Id = P.Id " +
            "LEFT JOIN BillDetail BD ON P.Id = BD.Product_Id " +
            "GROUP BY C.Id, C.Name, P.Quantity", nativeQuery = true)
    List<Object[]> getBillDetails();

    @Query(value = "SELECT TOP 5 P.Id AS Product_Id, P.Name AS Product_Name, " +
            "SUM(BD.Quantity) AS Total_Sold_Quantity " +
            "FROM Products P " +
            "INNER JOIN BillDetail BD ON P.Id = BD.Product_Id " +
            "GROUP BY P.Id, P.Name " +
            "ORDER BY SUM(BD.Quantity) DESC", nativeQuery = true)
    List<Object[]> findTop5BestSellingProducts();
}
