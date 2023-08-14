package com.greenhouse.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenhouse.model.BillDetail;

public interface BillDetailDAO extends JpaRepository<BillDetail, Integer> {
    @Query(value = "select b.Receiver_Fullname, b.Receiver_Phone, b.Receiver_Address, bd.Product_Id, p.Image, p.Name, bd.Price, bd.Quantity, bd.Amount "
            +
            "from Bills b " +
            "JOIN Bill_Detail bd ON b.Id = bd.Bill_Id " +
            "JOIN Products p on bd.Product_Id = p.Id " +
            "WHERE b.id = ?1 " +
            "GROUP BY b.Receiver_Fullname, b.Receiver_Phone, b.Receiver_Address, bd.Bill_Id, bd.Id, bd.Product_Id, bd.Quantity, bd.Price, bd.Amount, p.Name, p.Image", nativeQuery = true)
    List<Object[]> getBillDetailsForClient(String billId);
}
