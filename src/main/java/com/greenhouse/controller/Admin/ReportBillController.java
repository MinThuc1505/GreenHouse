package com.greenhouse.controller.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin/billReport")
public class ReportBillController {
    @GetMapping
	public String index() {
		return "admin/layouts/billReport";
	}
}
