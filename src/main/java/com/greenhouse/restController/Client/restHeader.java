package com.greenhouse.restController.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.AccountDAO;

@RestController
@RequestMapping(value = "/client/rest/header")
public class restHeader {

    @Autowired
    private AccountDAO accountDAO;

    @GetMapping("/quantityCart")
    private ResponseEntity<Map<String, Object>> getQtyCart(@RequestParam("username") String username) {
        Map<String, Object> responseData = new HashMap<>();
        int qtyCart = 0;

        if (accountDAO.existsById(username)) {
            qtyCart = accountDAO.getQtyCartByUsername(username);
        }

        responseData.put("qtyCart", qtyCart);
        return ResponseEntity.ok(responseData);
    }
}
