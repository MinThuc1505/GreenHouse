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

import com.greenhouse.DAO.CategoryDAO;
import com.greenhouse.DAO.ProductDAO;

@RestController
@RequestMapping(value = "/client/rest/shop")
public class restShop {

    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private CategoryDAO categoryDAO;

    @GetMapping
    private ResponseEntity<Map<String, Object>> getProducts() {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("products", productDAO.getProduct());
        responseData.put("categories", categoryDAO.findAll());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/filter")
    private ResponseEntity<Map<String, Object>> getProductsByCategoryAndPrice(
            @RequestParam("category") Optional<String> category, @RequestParam("price") Optional<String> priceString) {
        Map<String, Object> responseData = new HashMap<>();
        String cate = category.orElse(null);
        String price = priceString.orElse(null);

        if (cate != null && price != null) {
            responseData.put("products", productDAO.findByCategoryAndPrice(cate, price));
            System.out.println("findByCategoryAndPrice: " + cate + " " + price);
        } else {
            if (cate != null) {
                System.out.println("findByCategory: " + cate);
                responseData.put("products", productDAO.findByCategory(cate));
            } else if (price != null) {
                System.out.println("findByPrice: " + price);
                responseData.put("products", productDAO.findByPrice(price));
            }
        }
        responseData.put("categories", categoryDAO.findAll());
        return ResponseEntity.ok(responseData);
    }

}
