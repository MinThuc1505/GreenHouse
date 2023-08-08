package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greenhouse.model.Provider;

public interface ProviderDAO extends JpaRepository<Provider, String> {
    boolean existsById(Long id);

    Provider findByName(String name);
}
