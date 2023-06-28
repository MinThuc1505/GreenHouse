package com.greenhouse.DAO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenhouse.model.Product;

public interface ProductDAO extends JpaRepository<Product, String> {
	@Query("SELECT p.id, p.productName, p.price, p.price - (p.price * d.discountPercent / 100) , d.discountPercent, p.image ,p.description " +
                      "FROM Product p " +
                      "JOIN p.setDiscounts s " +
                      "JOIN s.discountId d " +
                      "WHERE CURRENT_DATE BETWEEN d.startDate AND d.endDate " +
                      "AND (d.quantity - s.quantityUsed) > 0 " +
                      "ORDER BY d.discountPercent DESC")
	List<Object[]> findTop10ProductsWithDiscounts(Pageable pageable);

	@Query("SELECT p.id, p.productName, p.price, p.price - (p.price * d.discountPercent / 100) , d.discountPercent, p.image, p.description "
			+
			"FROM Product p " +
			"JOIN p.setDiscounts s " +
			"JOIN s.discountId d " +
			"WHERE CURRENT_DATE BETWEEN d.startDate AND d.endDate " +
			"AND (d.quantity - s.quantityUsed) > 0 " +
			"ORDER BY d.discountPercent DESC")
	List<Object[]> getProductAnhDiscountPercent();

	@Query("SELECT p.id, p.productName, p.price, p.price - (p.price * d.discountPercent / 100) , d.discountPercent, p.image, p.description "
			+
			"FROM Product p " +
			"JOIN p.setDiscounts s " +
			"JOIN s.discountId d " +
			"JOIN p.setCategory sc " +
			"JOIN sc.categoryId c " +
			"WHERE CURRENT_DATE BETWEEN d.startDate AND d.endDate " +
			"AND (d.quantity - s.quantityUsed) > 0 " +
			"AND c.id = ?1")
	List<Object[]> getProductAnhDiscountPercentByCategory(String category);

	@Query("SELECT COUNT(*) FROM Product")
	int countProducts();

	@Query("SELECT SUM(d.quantity * p.price) AS totalRevenue " + "FROM Product p "
			+ "JOIN DetailBill d ON p.id = d.productId " + "JOIN Bill b ON b.id = d.bill")
	Double calculateTotalRevenue();

	@Query("SELECT c.categoryName, SUM(d.quantity) AS qua,SUM(d.quantity * d.price) " + "FROM Product p "
			+ "JOIN DetailBill d ON p.id = d.productId " + "JOIN Bill b ON b.id = d.bill.id " + "JOIN p.setCategory s "
			+ "JOIN s.categoryId c " + "GROUP BY c.categoryName " + "ORDER BY qua DESC")
	Page<Object[]> calculateCategoryRevenue(Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.productName LIKE %?1%"
			+ " OR p.description LIKE %?1%"
			+ " OR CAST(p.inStock AS string) LIKE %?1%"
			+ " OR CAST(p.price AS string) LIKE %?1%")
	Page<Product> findByKeyword(String keyword, Pageable pageable);

	@Query("SELECT b.id, FORMAT(b.billDate, 'dd/MM/yyyy HH:mm:ss'), COUNT(d.id) AS items, b.total "
			+ "FROM Product p "
			+ "JOIN DetailBill d ON d.productId = p.id "
			+ "JOIN Bill b ON b.id = d.bill "
			+ "JOIN Account a ON a.Id = b.accountId "
			+ "WHERE a.Id = :id " // Sử dụng "id" thay vì "accountId"
			+ "GROUP BY b.id, b.billDate, b.total "
			+ "ORDER BY b.billDate DESC")
	List<Object[]> getBillDetailsByAccountId(@Param("id") int id);

	@Query("SELECT p FROM Product p JOIN p.setCategory s JOIN Category c ON s.categoryId = c.id WHERE c.id = ?1 and p.inStock > 0")
	List<Product> findByCategory(String category);

	@Query("SELECT p FROM Product p JOIN p.setCategory s JOIN Category c ON s.categoryId = c.id WHERE c.id = ?1 AND p.price < ?2 and p.inStock > 0")
	List<Product> findByCategoryAndPrice(String categoryId, int price);

	@Query("SELECT p FROM Product p JOIN p.setCategory s JOIN Category c ON s.categoryId = c.id WHERE p.productName LIKE %?1% AND p.price < ?2 and p.inStock > 0")
	List<Product> findByKeywordAndPrice(String keyword, int price);

	@Query("SELECT p FROM Product p JOIN p.setCategory s JOIN Category c ON s.categoryId = c.id WHERE p.productName LIKE %?1% and p.inStock > 0")
	List<Product> findByKeyword(String keyword);

	@Query("SELECT p FROM Product p JOIN p.setCategory s JOIN Category c ON s.categoryId = c.id WHERE p.price < ?1 and p.inStock > 0")
	List<Product> findByPrice(int price);

	@Query("SELECT p FROM Product p JOIN Cart c ON c.productId = p.id JOIN Account a ON a.id = c.accountId WHERE a.username = ?1 and a.active = true")
	List<Product> findInCartByUsername(String username);

}
