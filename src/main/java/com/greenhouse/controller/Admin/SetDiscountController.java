package com.greenhouse.controller.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.greenhouse.DAO.CategoryDAO;
import com.greenhouse.DAO.DiscountDAO;
import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.DAO.SetDiscountDAO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Discount;
import com.greenhouse.model.Message;
import com.greenhouse.model.Product;
import com.greenhouse.model.SetDiscount;
import com.greenhouse.service.SessionService;

@Controller
@RequestMapping("/admin/setDiscount")
public class SetDiscountController {
	@Autowired
	private DiscountDAO discountDAO;
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private SetDiscountDAO setDiscountDAO;
	@Autowired
	private SessionService sessionService;

	private int sd_discountPage = 0;
	private int sd_productPage = 0;

	// SET_DISCOUNT - MINH THUC - START

	@GetMapping("")
	public String getDiscount(Model model) {
		Account account = sessionService.get("account");
		model.addAttribute("account", account);
		return setDiscount(model, new ArrayList<Discount>(), new ArrayList<Product>());
	}

	@GetMapping("/page")
	public String setDiscountPage(Model model, @RequestParam("pageP") Optional<Integer> pageP,
			@RequestParam("pageD") Optional<Integer> pageD) {
		Account account = sessionService.get("account");
		model.addAttribute("account", account);
		if (pageD.isPresent()) {
			sd_discountPage = pageD.get();
		}
		if (pageP.isPresent()) {
			sd_productPage = pageP.get();
		}
		return this.setDiscount(model, new ArrayList<Discount>(), new ArrayList<Product>());
	}

	@PostMapping("/discount/filter")
	public String getFilter(Model model, @RequestParam("discountPercent") Optional<String> discountPercent,
			@RequestParam("search") Optional<String> discountCode) {

		List<Discount> temp = discountDAO
				.findByKeyword(discountCode.orElse(""), Float.parseFloat(discountPercent.get()));

		List<Discount> discounts = new ArrayList<Discount>();
		for (Discount discount : temp) {
			if (discount.getEndDate().after(new Date())) {
				discounts.add(discount);
			}
		}
		Account account = sessionService.get("account");
		model.addAttribute("account", account);

		return setDiscount(model, discounts, new ArrayList<Product>());
	}

	@GetMapping("/discount/filter")
	public String getFilter() {
		return "redirect:/admin/setDiscount";
	}

	@PostMapping("/product/filter")
	public String getProductFilter(Model model, @RequestParam("price") int price,
			@RequestParam("category") String category) {
		Account account = sessionService.get("account");
		model.addAttribute("account", account);
		List<Product> products = new ArrayList<Product>();
		if (price == 0) {
			products = productDAO.findByCategory(category);
		} else {
			products = productDAO.findByCategoryAndPrice(category, price);
		}

		model.addAttribute("categoryOld", category);
		model.addAttribute("priceOld", price);

		return setDiscount(model, new ArrayList<Discount>(), products);
	}

	@GetMapping("/product/filter")
	public String getFilter1() {
		return "redirect:/admin/setDiscount";
	}

	@GetMapping("/apply")
	public String getOrder(Model model, @RequestParam(value = "productIDs", required = false) String checkedIDs,
			@RequestParam("idDiscountCode") String idDiscount) {
		Account account = sessionService.get("account");
		model.addAttribute("account", account);

		String[] checked = checkedIDs.split(",");
		var discount = discountDAO.findById(Integer.parseInt(idDiscount)).get();
		for (String string : checked) {
			var product = productDAO.findById(string).get();
			if (setDiscountDAO.checkDuplicates(product, discount) == null) {
				SetDiscount temp = new SetDiscount();
				temp.setProductId(product);
				temp.setDiscountId(discount);
				temp.setQuantityUsed(0);
				setDiscountDAO.save(temp);
				Message.message = "Áp dụng mã giảm thành công";
				Message.type = "success";
			} else {
				Message.message = "Mã Giảm đã được áp dụng";
				Message.type = "error";
			}
		}

		return "redirect:/admin/setDiscount";
	}

	@GetMapping("/table")
	public String getTable(Model model) {
		Account account = sessionService.get("account");
		model.addAttribute("account", account);
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		var discounts = setDiscountDAO.findAll();
		List<Object[]> temp = new ArrayList<Object[]>();
		for (SetDiscount setDiscount : discounts) {
			if (setDiscount.getDiscountId().getEndDate().after(new Date())) {
				Object[] obj = new Object[6];
				obj[0] = setDiscount.getId();

				Product product = setDiscount.getProductId();
				obj[1] = product.getProductName();

				Discount discount = setDiscount.getDiscountId();
				obj[2] = discount.getDiscountPercent();
				obj[3] = discount.getQuantity();
				obj[4] = df.format(discount.getStartDate());
				obj[5] = df.format(discount.getEndDate());

				temp.add(obj);
			}
		}

		model.addAttribute("setDiscounts", temp);

		model.addAttribute("template", "SetDiscountTable.html");
		model.addAttribute("fragment", "content");
		return "admin/main-layout";
	}
	// SET_DISCOUNT - MINH THUC - END

	private String setDiscount(Model model, List<Discount> discountList, List<Product> productList) {
		if (discountList.size() > 0 || !discountList.isEmpty()) {
			model.addAttribute("discounts", discountList);
		} else {
			// DISCOUNT ----------------
			Pageable page = PageRequest.of(sd_discountPage, 5);
			model.addAttribute("currentDiscountPage", sd_discountPage);

			var discounts = discountDAO.findAll(page);

			model.addAttribute("discounts", discounts);

			var numberOfRecords = discounts.getContent().size();
			var totalPages = (int) Math.ceil(numberOfRecords / 5.0);
			var startPage = (sd_discountPage - 2) >= 0 ? (sd_discountPage - 2) : 0;
			var endPage = (sd_discountPage + 2) < totalPages ? (sd_discountPage + 2) : totalPages - 1;
			model.addAttribute("startDiscountPage", startPage);
			model.addAttribute("endDiscountPage", endPage);
			model.addAttribute("totalDiscountPages", totalPages);

		}

		if (productList.size() > 0 || !productList.isEmpty()) {
			model.addAttribute("products", productList);
		} else {
			// PRODUCT ---------------------------

			Pageable pageProduct = PageRequest.of(sd_productPage, 5);
			model.addAttribute("currentProductPage", sd_productPage);
			var products = productDAO.findAll(pageProduct);

			var numberOfRecordsProduct = products.getContent().size();
			var totalProductPages = (int) Math.ceil(numberOfRecordsProduct / 5.0);

			var startProductPage = (sd_productPage - 2) >= 0 ? (sd_productPage - 2) : 0;
			var endProductPage = (sd_productPage + 2) < totalProductPages ? (sd_productPage + 2)
					: totalProductPages - 1;
			model.addAttribute("startProductPage", startProductPage);
			model.addAttribute("endProductPage", endProductPage);

			model.addAttribute("totalProductPages", totalProductPages);
			model.addAttribute("products", products);
		}
		var categories = categoryDAO.findCategoryNotLike("Phòng");
		model.addAttribute("categories", categories);

		model.addAttribute("message", Message.message);
		model.addAttribute("typeMessage", Message.type);
		Message.message = "";
		model.addAttribute("template", "SetDiscount.html");
		model.addAttribute("fragment", "content");
		return "admin/main-layout";
	}
}
