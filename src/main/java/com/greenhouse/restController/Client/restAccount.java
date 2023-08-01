package com.greenhouse.restcontroller.Client;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/client/rest/login")
public class restAccount {
	
	@GetMapping()
	public String checkLogin(Model model) {

		return "Xử lý đăng nhập";
	}
	
	@GetMapping(value = "/success")
	public String success(Model model) {
	
		return "đang nhập thành công";
	}
	
	@GetMapping(value = "/error")
	public String error(Model model) {
	
		return "đang nhập thất bại";
	}
}
