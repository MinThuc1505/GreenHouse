package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenhouse.model.Material;

public interface MaterialDAO extends JpaRepository<Material, Integer> {

}
