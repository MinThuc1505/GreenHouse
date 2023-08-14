package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenhouse.model.Size;

public interface SizeDAO extends JpaRepository<Size, Integer> {


    boolean existsBySize(String size);

}
