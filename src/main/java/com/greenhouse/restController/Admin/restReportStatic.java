package com.greenhouse.restController.Admin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.BillDAO;
import com.greenhouse.model.Bill;
import com.greenhouse.model.Discount;

@RestController
@RequestMapping(value = "/rest/MonthlyStatistic")
public class restReportStatic {

    @Autowired
    BillDAO billDAO;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<Object[]>> findTotalRevenueByTransactionDate(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        List<Object[]> revenueDetails = billDAO.findTotalRevenueByTransactionDateBetween(startDate, endDate);
        return ResponseEntity.ok(revenueDetails);
    }

    @CrossOrigin
    @GetMapping("/all")
    public ResponseEntity<List<Object[]>> findAllTotalRevenue() {
        List<Object[]> revenueDetails = billDAO.findAllTotalRevenue();
        return ResponseEntity.ok(revenueDetails);
    }

    @CrossOrigin
    @GetMapping("/monthly")
    public ResponseEntity<List<Object[]>> getMonthlyRevenue() {
        List<Object[]> monthlyRevenue = billDAO.getMonthlyRevenue();
        return ResponseEntity.ok(monthlyRevenue);
    }

    // @GetMapping("/page")
    // public ResponseEntity<Page<Bill>> findAllTotalRevenue(
    //         @RequestParam("page") Integer page,
    //         @RequestParam("size") Integer size) {
    //     try {
    //         System.out.println("Requested Page: " + page);
    //         System.out.println("Page Size: " + size);

    //         Pageable pageable = PageRequest.of(page, size);
    //         Page<Bill> bills = billDAO.findAllTotalRevenue(pageable);

    //         return ResponseEntity.ok(bills);
    //     } catch (Exception e) {
    //         // Xử lý các exception nếu cần thiết
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }
}
