package com.greenhouse.DAO;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenhouse.model.DetailBill;

public interface DetailBillDAO extends JpaRepository<DetailBill, Integer> {
        @Query("SELECT FORMAT(b.billDate, 'dd/MM/yyyy HH:mm:ss') , p.productName, SUM(d.quantity) AS totalQuantity, p.price AS unitPrice, SUM(d.quantity * p.price) AS totalPrice "
                        + "FROM Product p "
                        + "JOIN DetailBill d ON p.id = d.productId "
                        + "JOIN Bill b ON b.id = d.bill "
                        + "WHERE b.billDate >= :startDate AND b.billDate <= :endDate "
                        + "GROUP BY b.billDate, p.productName, p.price")
        // Page<Object[]> getListReport(@Param("startDate") Date startDate, @Param("endDate") Date endDate,Pageable pageable);
        List<Object[]> getListReport( Date startDate, Date endDate);


        
        @Query("SELECT YEAR(b.billDate) as year, SUM(b.total) as revenue FROM Bill b GROUP BY YEAR(b.billDate)")
        List<Object[]> getRevenueByYear();
}
