package com.greenhouse.controller.Client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client/checkout")
public class CheckoutController {

    @GetMapping
    public String getCart() {
        return "client/layouts/checkout";
    }

    @GetMapping("/donePay")
    public String getDonePay() {
        return "client/layouts/donePay";
    }

}
