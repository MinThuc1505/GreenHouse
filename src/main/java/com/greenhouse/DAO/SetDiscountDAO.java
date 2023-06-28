package com.greenhouse.DAO;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenhouse.model.Discount;
import com.greenhouse.model.Product;
import com.greenhouse.model.SetDiscount;

public interface SetDiscountDAO extends JpaRepository<SetDiscount, Integer> {
    @Query("SELECT s FROM SetDiscount s WHERE s.productId = ?1 and s.discountId = ?2")
    SetDiscount checkDuplicates(Product p, Discount d);

    @Query("SELECT s.productId.id, d.discountPercent FROM SetDiscount s JOIN Discount d ON s.id = d.id JOIN Product p ON s.productId = p.id WHERE p.id = ?1")
    List<Object> getDiscountPercentByProduct(String p);

    @Query("SELECT s FROM SetDiscount s JOIN s.discountId d WHERE CURRENT_DATE BETWEEN d.startDate AND d.endDate")
    Page<SetDiscount> findAll(Pageable pageable);
}