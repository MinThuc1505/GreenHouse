package com.greenhouse.restController.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.BillImportProductDAO;
import com.greenhouse.model.BillImportProduct;

@RestController
@RequestMapping(value = "/rest/billImportProduct")
public class restBillImport {
    
    @Autowired
    BillImportProductDAO billImportProductDAO;
       @GetMapping
    private ResponseEntity<List<BillImportProduct>> getAllImportProducts() {
        return ResponseEntity.ok(billImportProductDAO.findAll());
    }
     @GetMapping(value = "/{id}")
    private ResponseEntity<BillImportProduct> getOne(@PathVariable("id") Integer id) {
        if (!billImportProductDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(billImportProductDAO.findById(id).get());
    }

 
}
