package com.greenhouse.controller.Client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client/change-password")
public class ChangePasswordController {
    @GetMapping("")
    public String changePassword() {
        return "client/layouts/change-password";
    }
}
