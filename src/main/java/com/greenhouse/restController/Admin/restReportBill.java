package com.greenhouse.restController.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.BillDAO;
import com.greenhouse.model.Bill;
import com.greenhouse.model.Discount;


@RestController
@RequestMapping(value = "/rest/reportBill")
public class restReportBill {

	@Autowired
	BillDAO billDAO;
	
	@GetMapping
    private ResponseEntity<List<Bill>> getAllBill(){
        return ResponseEntity.ok(billDAO.findAll());
    }

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

    @GetMapping("/page")
    public ResponseEntity<Page<Bill>> getAllAccounts(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        try {
            System.out.println("Requested Page: " + page);
            System.out.println("Page Size: " + size);

            Pageable pageable = PageRequest.of(page, size);
            Page<Bill> bills = billDAO.findAll(pageable);

            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            // Xử lý các exception nếu cần thiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
