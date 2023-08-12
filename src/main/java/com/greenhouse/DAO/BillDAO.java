package com.greenhouse.DAO;

import com.greenhouse.model.Bill;
import com.greenhouse.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BillDAO extends JpaRepository<Bill, Integer> {

        @Query(value = "SELECT C.Id AS Category_Id, C.Name AS Category_Name, " +
                        "SUM(BD.Quantity) AS Sold_Quantity, SUM(BD.Amount) AS Total_Sales, " +
                        "(P.Quantity - COALESCE(SUM(BD.Quantity), 0)) AS Remaining_Quantity " +
                        "FROM Categories C " +
                        "INNER JOIN Set_Category SC ON C.Id = SC.Category_Id " +
                        "INNER JOIN Products P ON SC.Product_Id = P.Id " +
                        "LEFT JOIN Bill_Detail BD ON P.Id = BD.Product_Id " +
                        "GROUP BY C.Id, C.Name, P.Quantity", nativeQuery = true)
        List<Object[]> getBillDetails();

        // @Query(value = "SELECT TOP 5 P.Id AS Product_Id, P.Name AS Product_Name, " +
        // "SUM(BD.Quantity) AS Total_Sold_Quantity " +
        // "FROM Products P " +
        // "INNER JOIN Bill_Detail BD ON P.Id = BD.Product_Id " +
        // "GROUP BY P.Id, P.Name " +
        // "ORDER BY SUM(BD.Quantity) DESC", nativeQuery = true)
        // List<Object[]> findTop5BestSellingProducts();

        // @Query(value = "SELECT * FROM dbo.Bills WHERE Id = ?1", nativeQuery = true)
        // Bill getBillById(int id);
        // @Query(value = "SELECT TOP 5 P.Id AS Product_Id, P.Name AS Product_Name, " +
        //                 "SUM(BD.Quantity) AS Total_Sold_Quantity " +
        //                 "FROM Products P " +
        //                 "INNER JOIN Bill_Detail BD ON P.Id = BD.Product_Id " +
        //                 "GROUP BY P.Id, P.Name " +
        //                 "ORDER BY SUM(BD.Quantity) DESC", nativeQuery = true)
        // List<Object[]> findTop5BestSellingProducts();

        // @Query(value = "SELECT * FROM dbo.Bills WHERE Id = ?1", nativeQuery = true)
        // Bill getBillById(int id);

        @Query("SELECT YEAR(b.createDate) AS Year, MONTH(b.createDate) AS Month, DAY(b.createDate) AS Day, COUNT(b) AS Total_Bills, SUM(b.amount) AS Total_Revenue "
                        + "FROM Bill b "
                        + "WHERE b.createDate BETWEEN :startDate AND :endDate "
                        + "GROUP BY YEAR(b.createDate), MONTH(b.createDate), DAY(b.createDate) "
                        + "ORDER BY YEAR(b.createDate), MONTH(b.createDate), DAY(b.createDate) ASC")
        List<Object[]> findTotalRevenueByTransactionDateBetween(@Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

        @Query("SELECT YEAR(b.createDate) AS Year, MONTH(b.createDate) AS Month, DAY(b.createDate) AS Day, COUNT(b) AS Total_Bills, SUM(b.amount) AS Total_Revenue "
                        + "FROM Bill b "
                        + "WHERE b.status = true "
                        + "GROUP BY YEAR(b.createDate), MONTH(b.createDate), DAY(b.createDate) "
                        + "ORDER BY YEAR(b.createDate), MONTH(b.createDate), DAY(b.createDate) ASC")
        List<Object[]> findAllTotalRevenue();

        @Query("SELECT MONTH(b.createDate) AS Thang, SUM(b.newAmount) AS TongDoanhThu "
                        + "FROM Bill b "
                        + "WHERE b.status = true "
                        + "GROUP BY MONTH(b.createDate)")
        List<Object[]> getMonthlyRevenue();

        @Query("SELECT YEAR(b.createDate) AS Nam, SUM(b.newAmount) AS TongDoanhThu "
                        + "FROM Bill b "
                        + "WHERE b.status = true "
                        + "GROUP BY YEAR(b.createDate)")
        List<Object[]> getYearRevenue();

        @Query("SELECT SUM(b.newAmount) FROM Bill b WHERE b.status = 1")
        Long getTotalRevenue();

        @Query("SELECT SUM(d.quantity) FROM Discount d")
        Long getTotalDiscounts();

        @Query("SELECT COUNT(a) FROM Account a")
        Long getTotalUsers();

        @Query("SELECT COUNT(p) FROM Product p")
        Long getTotalProducts();

        @Query("SELECT P.id AS Product_Id, P.name AS Product_Name, P.price AS Product_Price, " +
                        "SUM(BD.quantity) AS Total_Sold_Quantity " +
                        "FROM Product P " +
                        "INNER JOIN BillDetail BD ON P.id = BD.product.id " +
                        "GROUP BY P.id, P.name, P.price " +
                        "ORDER BY Total_Sold_Quantity DESC " +
                        "LIMIT 5")
        List<Object[]> findTop5BestSellingProducts();

        @Query(value = "SELECT Role, COUNT(*) AS TotalCount FROM dbo.Accounts GROUP BY Role", nativeQuery = true)
        List<Object[]> getRoleAndTotalCount();

        @Query(value = "SELECT * FROM dbo.Bills WHERE Id = ?1", nativeQuery = true)
        Bill getBillById(int id);
}
