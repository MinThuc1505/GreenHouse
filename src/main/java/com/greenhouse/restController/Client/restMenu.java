package com.greenhouse.restController.Client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.CategoryDAO;

@RestController
@RequestMapping(value = "/client/rest/menu")
public class restMenu {
    @Autowired
    private CategoryDAO categoryDAO;

    @GetMapping
    private ResponseEntity<Map<String, Object>> getProducts() {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("categories", categoryDAO.findAll());
        return ResponseEntity.ok(responseData);
    }
}
