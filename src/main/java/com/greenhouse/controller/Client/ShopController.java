package com.greenhouse.controller.Client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client/shop")
public class ShopController {

    @GetMapping
    public String getShop() {
        return "client/layouts/shop";
    }

}
