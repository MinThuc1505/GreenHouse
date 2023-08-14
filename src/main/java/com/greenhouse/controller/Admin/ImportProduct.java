package com.greenhouse.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/importProduct")
public class ImportProduct {
    @GetMapping
	public String index() {
		return "admin/layouts/importProduct";
	}

}