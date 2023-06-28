package com.greenhouse.DAO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenhouse.model.Account;

public interface AccountDAO extends JpaRepository<Account, Integer> {
    @Query("SELECT a FROM Account a WHERE a.username = :username AND a.password = :password and a.active = 'true'")
    Account findBySignIn(String username, String password);

    @Query("SELECT a FROM Account a WHERE a.username = :username and a.active = true")
    Account checkDuplicateUsername(@Param("username") String username);

    @Query("SELECT a FROM Account a WHERE a.email = :email and a.active = true")
    Account checkDuplicateEmail(@Param("email") String email);

    @Query("SELECT a FROM Account a WHERE a.phone = :phone and a.active = true")
    Account checkDuplicatePhone(@Param("phone") String phone);

    @Query("SELECT a FROM Account a WHERE a.phone = :phone")
    Account findByPhone(@Param("phone") String phone);
    // Phương thức tìm kiếm Account theo keyword
    @Query("SELECT a FROM Account a WHERE " +
            "LOWER(a.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.address) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Account> findByKeyword(String keyword, Pageable pageable);

    @Query("SELECT COALESCE(SUM(c.quantity), 0) FROM Account a JOIN Cart c ON a.id = c.accountId WHERE a.id = ?1 and c.status = false")
    int findQuanityCartById(int id);


    @Query("SELECT a.email FROM Account a where a.active = true")
    List<String> getEmailAccountActive();
}
