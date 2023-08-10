package com.greenhouse.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenhouse.model.Set_Category;

public interface SetCategoryDAO extends JpaRepository<Set_Category, Integer> {
}
