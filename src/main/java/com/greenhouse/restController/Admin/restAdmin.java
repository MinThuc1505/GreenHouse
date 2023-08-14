package com.greenhouse.restController.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.BillDAO;

@RestController
@RequestMapping(value = "/rest/index")
public class restAdmin {

    @Autowired
    BillDAO billDAO;

    @GetMapping("/revenue")
    public ResponseEntity<Long> getTotalRevenue() {
        try {
            Long totalRevenue = billDAO.getTotalRevenue();
            return ResponseEntity.ok(totalRevenue);
        } catch (Exception e) {
            // Xử lý các exception nếu cần thiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/totalDiscounts")
    public ResponseEntity<Long> getTotalDiscounts() {
        try {
            Long totalDiscounts = billDAO.getTotalDiscounts();
            return ResponseEntity.ok(totalDiscounts);
        } catch (Exception e) {
            // Xử lý các exception nếu cần thiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/totalUsers")
    public ResponseEntity<Long> getTotalUsers() {
        try {
            Long totalUsers = billDAO.getTotalUsers();
            return ResponseEntity.ok(totalUsers);
        } catch (Exception e) {
            // Xử lý các exception nếu cần thiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/totalProducts")
    public ResponseEntity<Long> getTotalProducts() {
        try {
            Long totalProducts = billDAO.getTotalProducts();
            return ResponseEntity.ok(totalProducts);
        } catch (Exception e) {
            // Xử lý các exception nếu cần thiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/topProducts")
    public ResponseEntity<List<Object[]>> getTopSellingProducts() {
        try {
            List<Object[]> topSellingProducts = billDAO.findTop5BestSellingProducts();
            return ResponseEntity.ok(topSellingProducts);
        } catch (Exception e) {
            // Xử lý các exception nếu cần thiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/year")
    public ResponseEntity<List<Object[]>> getYearRevenue() {
        try {
            List<Object[]> topSellingProducts = billDAO.getYearRevenue();
            return ResponseEntity.ok(topSellingProducts);
        } catch (Exception e) {
            // Xử lý các exception nếu cần thiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/role")
    public ResponseEntity<List<Object[]>> getRoleAndTotalCount() {
        try {
            List<Object[]> countrole = billDAO.getRoleAndTotalCount();
            return ResponseEntity.ok(countrole);
        } catch (Exception e) {
            // Xử lý các exception nếu cần thiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    

}