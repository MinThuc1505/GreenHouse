package com.greenhouse.controller.Client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client/cart")
public class CartController {

    @GetMapping
    public String getCart() {
        return "client/layouts/cart";
    }
    
}
