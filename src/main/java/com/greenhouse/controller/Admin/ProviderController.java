package com.greenhouse.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin/provider")
public class ProviderController {
    @GetMapping
	public String index() {
		return "admin/layouts/provider";
	}
}
