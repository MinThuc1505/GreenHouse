package com.greenhouse.DAO;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenhouse.model.Cart;

public interface CartDAO extends JpaRepository<Cart, Integer> {
    @Query("SELECT c.id, p.image, p.productName, c.price, c.quantity from Cart c JOIN Product p ON p.id = c.productId JOIN Account a ON c.accountId = a.id WHERE a.username = ?1 and active = 'true' and c.status = 'false'")
    List<Object> findByAccountUsername(String username);

    @Query("SELECT c.id, p.image, p.productName, c.price, c.quantity from Cart c JOIN Product p ON p.id = c.productId JOIN Account a ON c.accountId = a.id WHERE c.id = ?1 and c.status = 'false'")
    List<Object> findByCartIdStatusFalse(int id);

    @Query("SELECT SUM(c.price * c.quantity) from Cart c JOIN Product p ON p.id = c.productId JOIN Account a ON c.accountId = a.id WHERE c.id = ?1 and c.status = 'false'")
    long getTotalCheckOutByIdCart(int id);

    @Query("SELECT c from Cart c JOIN Account a ON a.id = c.accountId Where c.productId = ?1 and a.id = ?2 and c.status = 'false'")
    Cart findByProductIdAndAccountId(String productId, int accountId);

    @Query("SELECT SUM(c.quantity) FROM Account a JOIN Cart c ON a.id = c.accountId WHERE a.username = ?1 and a.active = true")
    int countByAccountId(String username);

}
