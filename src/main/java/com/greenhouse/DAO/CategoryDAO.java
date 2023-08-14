package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenhouse.model.Category;

public interface CategoryDAO extends JpaRepository<Category, String> {

    Category findByName(String name);
    
}
