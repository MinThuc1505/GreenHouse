package com.greenhouse.DAO;
import org.springframework.data.jpa.repository.JpaRepository;
import com.greenhouse.model.Product;

public interface ProductDAO extends JpaRepository<Product, Integer> {

}
