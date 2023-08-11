package com.greenhouse.controller.Client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client/single-product")
public class SingleProductController {

    @GetMapping
    public String getSingleProduct() {
        return "client/layouts/single-product";
    }
    
}
