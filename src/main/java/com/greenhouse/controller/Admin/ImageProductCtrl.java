package com.greenhouse.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/imageProducts")
public class ImageProductCtrl {
    @GetMapping
	public String index() {
		return "admin/layouts/imageProduct";
	}

}