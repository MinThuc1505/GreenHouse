package com.greenhouse.controller.Admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenhouse.DAO.DiscountDAO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Discount;
import com.greenhouse.service.SessionService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/discount")
public class DiscountController {
	
	@GetMapping
	public String index() {
		return "admin/layouts/discount";
	}

}
