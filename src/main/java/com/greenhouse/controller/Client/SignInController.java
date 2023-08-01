package com.greenhouse.controller.Client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.greenhouse.model.Account;

@Controller
@RequestMapping("/client/signin")
public class SignInController {
	
	@GetMapping
	public String signin(Account account, Model model) {
		return "client/layouts/signin";
	}
}