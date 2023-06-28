package com.greenhouse.controller.Client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("client/error")
public class ErrorController {
    
    @GetMapping("")
    public String error(Model model){
        
     
        return "client/layouts/404";
    }
}
