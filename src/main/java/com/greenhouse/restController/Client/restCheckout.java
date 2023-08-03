package com.greenhouse.restController.Client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.CartDAO;

@RestController
@RequestMapping(value = "/client/rest/checkout")
public class restCheckout {

    @Autowired
    private CartDAO cartDAO;

    private String orderIds;

    @GetMapping
    private ResponseEntity<Map<String, Object>> getCart() {
        Map<String, Object> responseData = new HashMap<>();
        String status = "";
        String message = "";

        String[] checked = orderIds.split(";");
        var carts = new ArrayList<>();
        Long totalAmount = (long) 0;
        for (String string : checked) {
            var cart = cartDAO.getProductInCartByCartId(string);
            carts.add(cart);
            totalAmount += Long.parseLong(cartDAO.getAmountFromCart(string).toString());
        }

        if (carts.size() > 0) {
            status = "success";
            message = "Lấy dữ liệu lên trang thanh toán thành công";
            responseData.put("data", carts);
            responseData.put("totalAmount", totalAmount);
        } else {
            status = "error";
            message = "Không có sản phẩm nào trong giỏ hàng";
        }

        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/temp")
    private ResponseEntity<Map<String, Object>> getCart(@RequestParam String checkedIDsStr) {
        Map<String, Object> responseData = new HashMap<>();
        String status = "";
        String message = "";

        orderIds = checkedIDsStr;

        // String[] checked = checkedIDsStr.split(";");
        // var carts = new ArrayList<>();

        // for (String string : checked) {
        // var cart = cartDAO.getProductInCartByCartId(string);
        // carts.add(cart);
        // }
        // if(carts.size() > 0){
        // status="success";
        // message= "Lấy dữ liệu lên trang thanh toán thành công";
        // }else{
        // status ="error";
        // message= "Không có sản phẩm nào trong giỏ hàng";
        // }
        status = "success";
        message = "Lưu tạm carts id thành công";

        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }

}
