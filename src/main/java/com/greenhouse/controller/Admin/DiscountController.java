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
	@Autowired
	private DiscountDAO discountDAO;
	@Autowired
	private SessionService sessionService;
	private int discountPage = 0;

	@RequestMapping("")
	public String discount(Model model, @RequestParam("id") Optional<Integer> id) {

		var numberOfRecords = discountDAO.count();
		var totalPages = (int) Math.ceil(numberOfRecords / 8.0);

		var startPage = (discountPage - 2) >= 0 ? (discountPage - 2) : 0;
		var endPage = (discountPage + 2) < totalPages ? (discountPage + 2) : totalPages - 1;
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		model.addAttribute("totalPages", totalPages);
		Pageable page = PageRequest.of(discountPage, 8);
		model.addAttribute("currentPage", discountPage);

		var discounts = discountDAO.findAll(page);
		model.addAttribute("discounts", discounts);
		if (id.isPresent()) {
			model.addAttribute("discountEdit", discountDAO.findById(id.get()));
		} else {
			model.addAttribute("discountEdit", new Discount());
		}
		Account account = sessionService.get("account");
		model.addAttribute("account", account);
		model.addAttribute("template", "Discount.html");
		model.addAttribute("fragment", "content");
		return "admin/main-layout";
	}

	@GetMapping("/page")
	public String discountPage(Model model, @RequestParam("p") Optional<Integer> p,
			@RequestParam("id") Optional<Integer> id) {
		discountPage = p.orElse(0);
		Account account = sessionService.get("account");
		model.addAttribute("account", account);
		return this.discount(model, id);
	}

	@PostMapping("/create")
	public String discountCreate(Model model, @Valid @ModelAttribute("discountEdit") Discount discountCreate,
			BindingResult result, @RequestParam("id") Optional<Integer> id) {
		if (result.hasErrors()) {

			var numberOfRecords = discountDAO.count();
			var totalPages = (int) Math.ceil(numberOfRecords / 8.0);

			var startPage = (0 - 2) >= 0 ? (0 - 2) : 0;
			var endPage = (0 + 2) < totalPages ? (0 + 2) : totalPages - 1;
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);

			model.addAttribute("totalPages", totalPages);
			Pageable page = PageRequest.of(0, 8);
			model.addAttribute("currentPage", 0);

			var discounts = discountDAO.findAll(page);
			Account account = sessionService.get("account");
			model.addAttribute("account", account);
			model.addAttribute("discounts", discounts);
			model.addAttribute("template", "Discount.html");
			model.addAttribute("fragment", "content");
			return "admin/main-layout";
		} else if (discountCreate.getStartDate().after(discountCreate.getEndDate())) {
			model.addAttribute("successMessage", "Ngày bắt đầu không thể lớn hơn ngày kết thúc");
			var numberOfRecords = discountDAO.count();
			var totalPages = (int) Math.ceil(numberOfRecords / 8.0);

			var startPage = (0 - 2) >= 0 ? (0 - 2) : 0;
			var endPage = (0 + 2) < totalPages ? (0 + 2) : totalPages - 1;
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);

			model.addAttribute("totalPages", totalPages);
			Pageable page = PageRequest.of(0, 8);
			model.addAttribute("currentPage", 0);

			var discounts = discountDAO.findAll(page);
			Account account = sessionService.get("account");
			model.addAttribute("account", account);
			model.addAttribute("discounts", discounts);
			model.addAttribute("template", "Discount.html");
			model.addAttribute("fragment", "content");
			return "admin/main-layout";
		} else {
			model.addAttribute("successMessage", "Thêm thành công!");
			discountDAO.save(discountCreate);
		}

		return this.discount(model, id);
	}

	@GetMapping("/edit")
	public String edit(Model model, @RequestParam("id") Optional<Integer> id,
			@RequestParam("direct") Optional<Boolean> ASC, @RequestParam("field") Optional<String> field) {

		Account account = sessionService.get("account");
		model.addAttribute("account", account);

		return this.discount(model, id);
	}

	@PostMapping("/update")
	public String discountUpdate(Model model, @RequestParam("id") Optional<Integer> id,
			@Valid @ModelAttribute("discountEdit") Discount discountUpdate,
			BindingResult result) {

		if (result.hasErrors()) {

			var numberOfRecords = discountDAO.count();
			var totalPages = (int) Math.ceil(numberOfRecords / 8.0);

			var startPage = (0 - 2) >= 0 ? (0 - 2) : 0;
			var endPage = (0 + 2) < totalPages ? (0 + 2) : totalPages - 1;
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);

			model.addAttribute("totalPages", totalPages);
			Pageable page = PageRequest.of(0, 8);
			model.addAttribute("currentPage", 0);

			var discounts = discountDAO.findAll(page);
			Account account = sessionService.get("account");
			model.addAttribute("account", account);
			model.addAttribute("discounts", discounts);
			model.addAttribute("template", "Discount.html");
			model.addAttribute("fragment", "content");
			return "admin/main-layout";
		} else if (discountUpdate.getStartDate().after(discountUpdate.getEndDate())) {
			model.addAttribute("successMessage", "Ngày bắt đầu không thể lớn hơn ngày kết thúc");
			var numberOfRecords = discountDAO.count();
			var totalPages = (int) Math.ceil(numberOfRecords / 8.0);

			var startPage = (0 - 2) >= 0 ? (0 - 2) : 0;
			var endPage = (0 + 2) < totalPages ? (0 + 2) : totalPages - 1;
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);

			model.addAttribute("totalPages", totalPages);
			Pageable page = PageRequest.of(0, 8);
			model.addAttribute("currentPage", 0);

			var discounts = discountDAO.findAll(page);
			Account account = sessionService.get("account");
			model.addAttribute("account", account);
			model.addAttribute("discounts", discounts);
			model.addAttribute("template", "Discount.html");
			model.addAttribute("fragment", "content");
			return "admin/main-layout";
		} else {
			model.addAttribute("successMessage", "Cập nhật thành công!");
			discountDAO.save(discountUpdate);
			model.addAttribute("discountEdit", new Discount());
			
		}
		return this.discount(model, id);

	}

	@PostMapping("/delete")
	public String discountDelete(Model model, @RequestParam("id") Optional<Integer> id,
			@RequestParam("field") Optional<String> field, @RequestParam("direct") Optional<Boolean> ASC) {
		discountDAO.deleteById(id.orElse(null));
		return this.discount(model, id);
	}

}
