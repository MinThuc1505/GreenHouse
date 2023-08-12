package com.greenhouse.restController.Client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.model.Product;
import com.greenhouse.model.Set_Category;
import com.greenhouse.service.TextSimilarityService;

@RestController
@RequestMapping(value = "/client/rest/single-product")
public class restSingleProduct {

    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private TextSimilarityService textSimilarityService;

    @GetMapping(value = "/getProduct")
    private ResponseEntity<Map<String, Object>> getProduct(@RequestParam String productId) {
        Map<String, Object> responseData = new HashMap<>();
        String status = "";
        String message = "";
        Product product = productDAO.findById(Integer.parseInt(productId)).orElse(null);
        if (product != null) {
            String size = product.getSize().getSize();
            String material = product.getMaterial().getMaterial();
            responseData.put("product", product);
            responseData.put("size", size);
            responseData.put("material", material);
            status = "success";
            message = "Đã lấy lên sản phẩm từ API: " + product;
        } else {
            status = "error";
            message = "Không có dữ liệu trả về!";
        }
        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping(value = "/getRecommendProduct")
    private ResponseEntity<Map<String, Object>> getRecommendProduct(@RequestParam String productId) {
        Map<String, Object> responseData = new HashMap<>();
        String status = "";
        String message = "";

        List<Product> recommendedProducts = new ArrayList<>();
        Product product = productDAO.findById(Integer.parseInt(productId)).orElse(null);
        List<Product> products = productDAO.findAll();
        if (product != null) {
            products.remove(product);
            for (Product p : products) {
                String pDescription = p.getDescription();
                String description = product.getDescription();
                double similarityScore = textSimilarityService.calculateCosineSimilarity(pDescription, description);
                System.out.println("Phần trăm tương đồng của :"+ p.getName() +": " + similarityScore + "%");
                if (similarityScore >= 50) {
                    recommendedProducts.add(p);
                }
            }
            status = "success";
            message = "Đã tìm ra số sản phẩm tương đồng: " + recommendedProducts.size();
        }

        responseData.put("recommendedProducts", recommendedProducts);
        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }
}
