package com.greenhouse.restController.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.BillDAO;
import com.greenhouse.model.Bill;


@RestController
@RequestMapping(value = "/rest/reportCategory")
public class restReportCategory {

    @Autowired
    BillDAO billDAO;

    @GetMapping
    private ResponseEntity<List<Object[]>> getAllBillDetails() {
        List<Object[]> billDetails = billDAO.getBillDetails();
        return ResponseEntity.ok(billDetails);
    }
}
