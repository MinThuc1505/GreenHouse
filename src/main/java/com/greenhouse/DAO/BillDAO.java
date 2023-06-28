package com.greenhouse.DAO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.greenhouse.model.Bill;

public interface BillDAO extends JpaRepository<Bill, Integer> {
	@Query("SELECT b.id, a.username, b.total, FORMAT(b.billDate, 'dd/MM/yyyy HH:mm:ss') " + "FROM Bill b " + "JOIN DetailBill d ON b.id = d.bill.id "
			+ "JOIN Account a ON a.id = b.accountId " + "GROUP BY b.id, a.username, b.total, b.billDate "
			+ "ORDER BY b.billDate DESC")
	Page<Object[]> getBillDetails(Pageable pageable);

	@Query("SELECT b.id, a.username, b.total, FORMAT(b.billDate, 'dd/MM/yyyy HH:mm:ss')  " +
			"FROM Bill b " +
			"JOIN DetailBill d ON b.id = d.bill.id " +
			"JOIN Account a ON a.id = b.accountId " +
			"WHERE " +
			"LOWER(CONCAT(b.id, a.username, b.total, b.billDate)) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
			"GROUP BY b.id, a.username, b.total, b.billDate " +
			"ORDER BY b.billDate DESC")
	Page<Object[]> getBillDetailsbyKeyword(@Param("keyword") String keyword,Pageable pageable);

	@Query("SELECT b.id,  b.billDate, d.quantity, p.price, SUM(d.price*d.quantity), p.productName "
			+ "FROM Bill b "
			+ "JOIN DetailBill d ON b.id = d.bill "
			+ "JOIN Product p ON d.productId = p.id "
			+ "WHERE b.id = :billId "
			+ "GROUP BY b.id, b.billDate, d.quantity, p.price, d.price, p.productName")
	List<Object[]> getBillDetailsByBillId(@Param("billId") int billId);

	@Query("SELECT b.id ,a.fullname,b.total FROM Bill b  JOIN Account a "
			+ "on a.id=b.accountId "
			+ " WHERE b.id = :billId ")
	List<Object[]> getBillUsername(@Param("billId") int billId);
}
