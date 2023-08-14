package com.greenhouse.restController.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.PriceHistoryDAO;
import com.greenhouse.model.PriceHistory;


@RestController
@RequestMapping(value = "/rest/priceHistorys")
public class restPriceHistory {
    
    @Autowired
    PriceHistoryDAO priceHistoryDAO;
     @GetMapping
    private ResponseEntity<List<PriceHistory>> getAllPriceHistorys(){
        return ResponseEntity.ok(priceHistoryDAO.findAll());
    }
     @GetMapping(value = "/{id}")
    private ResponseEntity<PriceHistory> getOne(@PathVariable("id") Integer id) {
        if (!priceHistoryDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(priceHistoryDAO.findById(id).get());
    }

}
