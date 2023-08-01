package com.greenhouse.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenhouse.model.Account;

public interface AccountDAO extends JpaRepository<Account, String> {
    @Query(value = "SELECT * FROM dbo.Accounts WHERE Username = ?1 AND Password = ?2", nativeQuery = true)
    Account findByUsernameAndPassword(String username, String password);

    @Query(value = "SELECT SUM(db.Quantity) FROM dbo.Accounts acc "
    +"JOIN dbo.Bills b ON b.Username = acc.Username "
    +"JOIN dbo.BillDetail db ON db.Bill_Id = b.Id "
    +"WHERE acc.Username = ?1 GROUP BY acc.Username", nativeQuery = true)
    int getQtyCartByUsername(String username);
}
