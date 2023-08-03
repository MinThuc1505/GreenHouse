package com.greenhouse.DAO;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenhouse.model.Cart;

public interface CartDAO extends JpaRepository<Cart, Integer> {

    @Query(value = "SELECT * FROM dbo.Carts WHERE Username = ?1 AND Product_Id = ?2 AND Status = 'true'", nativeQuery = true)
    Cart getCart(String username, String productId);

    @Query(value = "SELECT c.Id ,p.Image, p.Name, c.Price, c.Quantity, c.Amount FROM dbo.Carts c "
    +"JOIN dbo.Products p ON p.Id = c.Product_Id  "
    +"WHERE c.Username = ?1 AND c.Status = 'true'", nativeQuery = true)
    Object[] getProductInCartByUsername(String username);

    @Query(value = "SELECT c.Id ,p.Image, p.Name, c.Price, c.Quantity, c.Amount FROM dbo.Carts c "
    +"JOIN dbo.Products p ON p.Id = c.Product_Id  "
    +"WHERE c.Id = ?1 AND c.Status = 'true'", nativeQuery = true)
    Object[] getProductInCartByCartId(String cartId);

    @Query(value = "SELECT c.Amount FROM dbo.Carts c "
    +"WHERE c.Id = ?1 AND c.Status = 'true'", nativeQuery = true)
    Long getAmountFromCart(String cartId);
}
