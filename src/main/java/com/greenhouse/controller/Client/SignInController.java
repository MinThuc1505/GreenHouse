package com.greenhouse.controller.Client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client/signin")
public class SignInController {
	
    @GetMapping
	public String index() {
		return "client/layouts/signin";
	}

}