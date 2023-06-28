package com.greenhouse.DAO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenhouse.model.Category;

public interface CategoryDAO extends JpaRepository<Category, String> {
    @Query(value="select c from Category c where c.categoryName NOT LIKE %?1%")
    List<Category> findCategoryNotLike(String keyword);

    @Query(value="select c from Category c where c.categoryName LIKE %?1%")
    List<Category> findCategoryLike(String keyword);

    @Query("SELECT c FROM Category c WHERE c.categoryName LIKE %:keyword% OR c.id LIKE %:keyword%")
    Page<Category> findByKeyword(String keyword, Pageable pageable);
     @Query("SELECT c FROM Category c WHERE c.categoryName = :categoryName")
    Category findByCategoryName(String categoryName);
}
