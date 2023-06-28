package com.greenhouse.controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenhouse.model.Account;
import com.greenhouse.service.SessionService;

@Controller
@RequestMapping("/admin/index")
public class IndexAdminController {
	@Autowired
	private SessionService sessionService;

	@GetMapping("")
	public String index(Model model) {
		Account account = sessionService.get("account");
		model.addAttribute("account", account);
		model.addAttribute("template", "index.html");
		model.addAttribute("fragment", "content");
		return "admin/main-layout";
	}

}
