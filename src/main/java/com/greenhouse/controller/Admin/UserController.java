package com.greenhouse.controller.Admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Message;
import com.greenhouse.service.EncodeService;
import com.greenhouse.service.SessionService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("admin")
public class UserController {
	@Autowired
	AccountDAO dao;
	@Autowired
	SessionService sessionService;
	private String staticDir = System.getProperty("user.dir") + "/src/main/resources/static";

	@RequestMapping("/user")
	public String account(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {

		int pageSize = 5; // Số lượng mục trên mỗi trang
		Page<Account> accountPage = getPageableAccounts(page, pageSize);

		List<Account> accounts = accountPage.getContent();
		int totalPages = accountPage.getTotalPages();
		int currentPage = accountPage.getNumber();
		int[] startAndEndPages = calculateStartAndEndPages(totalPages, currentPage);
		MultipartFile file = null;
		model.addAttribute("tempImage", file);

		model.addAttribute("message", Message.message);
		model.addAttribute("typeMessage", Message.type);
		Message.message = "";
		Account account = sessionService.get("account");
		model.addAttribute("account", account);
		model.addAttribute("account", new Account());
		model.addAttribute("accounts", accounts);
		model.addAttribute("template", "User.html");
		model.addAttribute("fragment", "content");
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("startPage", startAndEndPages[0]);
		model.addAttribute("endPage", startAndEndPages[1]);

		return "admin/main-layout";
	}

	private Page<Account> getPageableAccounts(int page, int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return dao.findAll(pageable);
	}

	private int[] calculateStartAndEndPages(int totalPages, int currentPage) {
		int startPage = Math.max(0, currentPage - 2);
		int endPage = Math.min(totalPages - 1, currentPage + 2);
		return new int[] { startPage, endPage };
	}

	private void saveImage(MultipartFile image, String directory, String filename) throws IOException {
		Path targetDir = Path.of(directory + "/images");
		Path targetFile = targetDir.resolve(filename);
		Files.copy(image.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
		String extension = FilenameUtils.getExtension(image.getOriginalFilename());
		filename = UUID.randomUUID().toString() + "." + extension;
		System.out.println(filename);
	}

	@PostMapping("/user/save")
	public String saveAccount(@Valid @ModelAttribute("account") Account account, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam("tempImage") MultipartFile tempImage, @RequestParam("oldPassword") String oldPassword)
			throws IOException {

		if (result.hasErrors()) {

			model.addAttribute("accounts", dao.findAll());
			model.addAttribute("template", "User.html");
			model.addAttribute("fragment", "content");
			return "admin/main-layout";
		}
		if (!account.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$")) {
			Message.message = "Mật khẩu không chứa ký tự đặc biệt và phải bao gồm chữ in hoa, số và ít nhất 6 ký tự";
			Message.type = "danger";
			return "redirect:/admin/user";
		} else {
			if (!oldPassword.isBlank()) {
				if (!account.getPassword().isBlank()) {
					String encryptedPassword = EncodeService.hashBcrypt(account.getPassword());
					account.setPassword(encryptedPassword);
				} else {
					String encryptedPassword = oldPassword;
					account.setPassword(encryptedPassword);
				}
				if (tempImage.getOriginalFilename() != "") {
					saveImage(tempImage, staticDir, tempImage.getOriginalFilename());
					account.setImage(tempImage.getOriginalFilename());
				}
				dao.save(account);

				redirectAttributes.addFlashAttribute("successMessage", "Lưu thành công!");
				return "redirect:/admin/user/edit?id=" + account.getId() + "&page=" + page;
			}
		}
		if (tempImage.getOriginalFilename() != "") {
			saveImage(tempImage, staticDir, tempImage.getOriginalFilename());
			account.setImage(tempImage.getOriginalFilename());
		}
		dao.save(account);

		redirectAttributes.addFlashAttribute("successMessage", "Lưu thành công!");
		return "redirect:/admin/user/edit?id=" + account.getId() + "&page=" + page;
	}

	@PostMapping("/user/delete")
	public String removeProduct(@Valid @ModelAttribute("account") Account account, BindingResult result,
			RedirectAttributes redirectAttributes, @RequestParam(value = "page", defaultValue = "0") int page) {

		Account currentUser = sessionService.get("account");

		if (currentUser != null && currentUser.getId() == account.getId()) {
			// Người dùng đang cố gắng xóa chính tài khoản của họ
			redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa chính bạn!");
		} else if (account.isRole()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa Admin!");

		} else {
			try {
				dao.delete(account);
				redirectAttributes.addFlashAttribute("successMessage", "Xóa thành công!");
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("errorMessage", "Tài khoản đang được sử dụng!");
			}
		}

		return "redirect:/admin/user?page=" + page;
	}

	@GetMapping("/user/edit")
	public String edit(@RequestParam("id") Integer id, @RequestParam(value = "page", defaultValue = "0") int page,
			Model model) {
		Optional<Account> optionalAccount = dao.findById(id);

		if (optionalAccount.isPresent()) {
			Account account = optionalAccount.get();
			model.addAttribute("oldPassword", account.getPassword());
			account.setPassword("");
			model.addAttribute("imageSrc", "/images/" + account.getImage());

			boolean role = account.isRole();
			model.addAttribute("account", account);
			model.addAttribute("role", role);
			model.addAttribute("template", "User.html");
			model.addAttribute("fragment", "content");
			model.addAttribute("currentPage", page);

			int pageSize = 5; // Số lượng mục trên mỗi trang
			Page<Account> accountPage = getPageableAccounts(page, pageSize);
			List<Account> accounts = accountPage.getContent();
			model.addAttribute("accounts", accounts);

			int[] startAndEndPages = calculateStartAndEndPages(accountPage.getTotalPages(), page);
			model.addAttribute("totalPages", accountPage.getTotalPages());
			model.addAttribute("startPage", startAndEndPages[0]);
			model.addAttribute("endPage", startAndEndPages[1]);
		} else {
			// Xử lý khi không tìm thấy tài khoản với id tương ứng
		}

		return "admin/main-layout";
	}

	@GetMapping("/user/sort")
	public String sortAccounts(Model model, @RequestParam("field") String field,
			@RequestParam(value = "direct", defaultValue = "ASC") String direct,
			@RequestParam(value = "page", defaultValue = "0") int page) {

		String sortDirection = direct.equalsIgnoreCase("ASC") ? "DESC" : "ASC";

		int pageSize = 5; // Số lượng mục trên mỗi trang
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.fromString(direct), field));
		Page<Account> accountPage = dao.findAll(pageable);

		int totalPages = accountPage.getTotalPages();
		int currentPage = accountPage.getNumber();
		int[] startAndEndPages = calculateStartAndEndPages(totalPages, currentPage);

		model.addAttribute("sortDirection", sortDirection);
		model.addAttribute("account", new Account());
		model.addAttribute("accounts", accountPage.getContent());
		model.addAttribute("template", "User.html");
		model.addAttribute("fragment", "content");
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("startPage", startAndEndPages[0]);
		model.addAttribute("endPage", startAndEndPages[1]);

		return "admin/main-layout";
	}

	@GetMapping("/user/search")
	public String searchAccounts(Model model, @RequestParam("keyword") String keyword,
			@RequestParam(value = "page", defaultValue = "0") int page) {
		int pageSize = 5; // Số lượng mục trên mỗi trang
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Account> accountPage = dao.findByKeyword(keyword, pageable);

		List<Account> accounts = accountPage.getContent();

		int totalPages = accountPage.getTotalPages();
		int currentPage = accountPage.getNumber();

		int[] startAndEndPages = calculateStartAndEndPages(totalPages, currentPage);

		model.addAttribute("account", new Account());
		model.addAttribute("accounts", accounts);
		model.addAttribute("template", "User.html");
		model.addAttribute("fragment", "content");
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("startPage", startAndEndPages[0]);
		model.addAttribute("endPage", startAndEndPages[1]);
		return "admin/main-layout";
	}
}
