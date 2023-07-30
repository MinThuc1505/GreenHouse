package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenhouse.model.SetCategory;

public interface SetCategoryDAO extends JpaRepository<SetCategory,Integer>{
  
}
