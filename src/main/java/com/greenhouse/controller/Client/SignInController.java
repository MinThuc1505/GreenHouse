package com.greenhouse.controller.Client;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
	
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();

			// Bây giờ bạn có thể sử dụng thông tin userDetails
			String username = userDetails.getUsername();
			
			Account account = accountDAO.findById(username).get();
			
			cookieService.setCookie(response, "username", account.getUsername().replaceAll("\\s", ""), 3600);
			System.out.println("Đăng nhập local thành công");
			return "redirect:/client/index";
		} else {
			return "redirect:/client/signin";
		}
	}
	
	@GetMapping("/client/signin/successGoogle")
	public String successGoogle(Model model, HttpServletResponse response,OAuth2AuthenticationToken auth2AuthenticationToken) {
		
		Account account = new Account();
		if (auth2AuthenticationToken != null && auth2AuthenticationToken.isAuthenticated()) {
	        OAuth2User oauth2User = auth2AuthenticationToken.getPrincipal();

	        // Lấy thông tin từ OAuth2User
	        String username =  oauth2User.getName();
	        String password = Long.toHexString(System.currentTimeMillis());
			String fulname = (String) oauth2User.getAttributes().get("name");
			String email = (String) oauth2User.getAttributes().get("email");														
			String pictureUrl = (String) oauth2User.getAttributes().get("picture"); 
						
			
			UserDetails user = User.withUsername(username).password(password).roles("USER").build();

	        // Tạo lại Authentication với vai trò mới
	        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
	        // Cập nhật Authentication
	        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
	        
	        account.setUsername(email);
	        account.setFullName(fulname);
	        account.setEmail(email);
	        account.setImage(pictureUrl);
	        account.setRole(newAuthentication.toString().contains("USER") ? false : true);
	        account.setCreateDate(new Date());
	        accountDAO.save(account);
	        
			System.out.println("Đăng nhập Google thành công");
			cookieService.setCookie(response, "username", email.replaceAll("\\s", ""), 3600);
	        return "redirect:/client/index";
	    }else {
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