package com.greenhouse.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin/categoryStatistics")
public class ReportCategoryController {
    @GetMapping
	public String index() {
		return "admin/layouts/categoryStatistics";
	}
}
