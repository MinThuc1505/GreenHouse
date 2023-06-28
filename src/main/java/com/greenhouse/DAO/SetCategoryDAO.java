package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenhouse.model.SetCategory;

public interface SetCategoryDAO extends JpaRepository<SetCategory,Integer>{
    // @Query("SELECT sc.categoryId, c.categoryName, SUM(d.quantity) AS totalSold, p.price, SUM(d.quantity * p.price) AS total "
    //         + "FROM SetCategory sc "
    //         + "JOIN sc.product p "
    //         + "JOIN sc.category c "
    //         + "JOIN sc.detailsBill d "
    //         + "GROUP BY sc.categoryId, c.categoryName, p.price "
    //         + "ORDER BY totalSold DESC")
    // List<Object[]> getCategorySales();
}
