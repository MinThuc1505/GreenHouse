package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenhouse.model.Account;

public interface AccountDAO extends JpaRepository<Account, String> {
    @Query(value = "SELECT * FROM dbo.Accounts WHERE Username = ?1 AND Password = ?2", nativeQuery = true)
    Account findByUsernameAndPassword(String username, String password);

    @Query(value = "SELECT SUM(c.Quantity) FROM dbo.Accounts acc "
    +"JOIN dbo.Carts c ON c.Username = acc.Username "
    +"WHERE acc.Username = ?1 AND c.Status = 'true'", nativeQuery = true)
    Integer getQtyCartByUsername(String username);
    
    Account findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
