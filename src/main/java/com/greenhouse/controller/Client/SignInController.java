package com.greenhouse.controller.Client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Message;
import com.greenhouse.service.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Controller
public class SignInController {
	@Autowired
	private CookieService cookieService;
	@Autowired
	private AccountDAO accountDAO;
		
	@GetMapping("/client/signin")
	public String signin(Account account, Model model) {
		String username = cookieService.getValue("username");
		account.setUsername(username);
		model.addAttribute("message", Message.message);
		model.addAttribute("typeMessage", Message.type);
		Message.message = "";
		model.addAttribute("account", account);
		return "client/layouts/signin";
	}
	
	@GetMapping("/client/signin/success")
	public String success(Model model, HttpServletResponse response) {

		// Lấy thông tin người dùng đã đăng nhập từ SecurityContextHolder
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		System.out.println(authentication);
		
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();

			// Bây giờ bạn có thể sử dụng thông tin userDetails
			String username = userDetails.getUsername();
			
			Account account = accountDAO.findById(username).get();
			
			cookieService.setCookie(response, "username", account.getUsername(), 3600);

			System.out.println("Đăng nhập thành công");
			return "redirect:/client/index";
		} else {
			return "redirect:/client/signin";
		}

	}
	
	@GetMapping("/client/signin/error")
	public String error(RedirectAttributes redirectAttributes,HttpServletRequest request) {	
		System.out.println("Đăng nhập thất bại");
		redirectAttributes.addFlashAttribute("loginMessage", "Sai tài khoản hoặc mật khẩu");
		return "redirect:/client/signin";
	}
	
	@GetMapping("/client/logout/success")
	public String logout(Model model,HttpServletResponse response) {
		cookieService.remove("username",response);
		System.out.println("Đăng xuất thành công");
		return "redirect:/client/signin";
	}
	
	@GetMapping("/client/denied")
	public String denied(Model model) {
		System.out.println("Chú không có tuổi");
		return "redirect:/client/error";
	}

}