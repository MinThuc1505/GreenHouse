package com.greenhouse.restcontroller.Client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.ProductDAO;

@RestController
@RequestMapping(value = "/client/rest/index")
public class restIndex {

    @Autowired
    private ProductDAO productDAO;

    @GetMapping
    private ResponseEntity<Map<String, Object>> get10Products() {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("products", productDAO.findTop10ProductsBestSale());
        return ResponseEntity.ok(responseData);
    }
    

}
