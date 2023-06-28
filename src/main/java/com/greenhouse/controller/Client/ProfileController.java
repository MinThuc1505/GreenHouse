package com.greenhouse.controller.Client;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.model.Account;
import com.greenhouse.service.SessionService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/client/profile")
public class ProfileController {
	@Autowired
	SessionService sessionService;
	@Autowired
	AccountDAO accountDAO;
	@Autowired
	ProductDAO productDAO;

	@GetMapping("")
	public String index(Model model) {
		Account account = sessionService.get("account");
		if (account != null) {
			model.addAttribute("account", account);
			model.addAttribute("imageSrc", "/images/" + account.getImage());
			int accountId = account.getId();
			// System.out.println("=============="+accountId);
			List<Object[]> listBill = productDAO.getBillDetailsByAccountId(accountId);
			System.out.println();
			model.addAttribute("listBill", listBill);
		}
		 // User Session - start
		 Account acc = sessionService.get("account");
		 if (acc != null) {
			 try {
				 int qty = accountDAO.findQuanityCartById(acc.getId());
				 model.addAttribute("qtyCart", qty);
			 } catch (Exception e) {
				 model.addAttribute("qtyCart", 0);
			 }
			 model.addAttribute("sessionUsername", acc.getUsername());
		 }
		 // User Session - end
		model.addAttribute("template", "profile.html");
		model.addAttribute("fragment", "content");
		return "client/main-layout";

	}

	@PostMapping("")
	public String edit_profile(@Valid @ModelAttribute("account") Account account, BindingResult result,
			Model model, @RequestParam("image1") MultipartFile image1) {
		if (result.hasFieldErrors()) {
			model.addAttribute("template", "profile.html");
			model.addAttribute("fragment", "content");
			return "client/main-layout";
		}

		if (image1.getOriginalFilename() != "") {
			try {
				// Lấy đường dẫn thư mục static
				String staticDir = System.getProperty("user.dir") + "/src/main/resources/static";

				// Tạo đường dẫn tới thư mục images trong static
				Path targetDir = Path.of(staticDir + "/images");

				// Tạo tên file ngẫu nhiên
				// String extension = FilenameUtils.getExtension(image1.getOriginalFilename());
				// String filename = UUID.randomUUID().toString() + "." + extension;
				String filename = image1.getOriginalFilename();
				// Tạo đường dẫn tới file mới
				Path targetFile = targetDir.resolve(filename);

				// Sao chép file vào đường dẫn mới
				Files.copy(image1.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
				System.out.println(filename);

				// Lưu tên file hình ảnh vào thuộc tính images của đối tượng Account
				account.setImage(filename);
			} catch (Exception e) {
				// Xử lý lỗi nếu có
				e.printStackTrace();
			}
		}
		Account account2 = accountDAO.findById(account.getId()).get();
		account.setRole(account2.isRole());
		account.setActive(true);
		accountDAO.save(account);
		sessionService.remove("account");
		sessionService.remove("username");
		sessionService.set("account", account);
		sessionService.set("username", account.getUsername());
		return "redirect:/client/profile";
	}

}
