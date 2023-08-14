package com.greenhouse.controller.Client;


import java.text.Normalizer;
import java.util.Date;
import java.util.regex.Pattern;

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
import com.greenhouse.service.CookieService;
import com.greenhouse.service.SessionService;

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
			System.out.println("Đăng nhập LOCAL thành công");
			return "redirect:/client/index";
		} else {
			return "redirect:/client/signin";
		}
	}
	
	@GetMapping("/client/social/success")
	public String socialSuccess(Model model, HttpServletResponse response,OAuth2AuthenticationToken auth2AuthenticationToken) {
		String username = null;
		String fulname = null;
		String email = null;
		String pictureUrl = null;
		
		Account account = new Account();
		if (auth2AuthenticationToken != null && auth2AuthenticationToken.isAuthenticated()) {
	        OAuth2User oauth2User = auth2AuthenticationToken.getPrincipal();
	        String registrationId = auth2AuthenticationToken.getAuthorizedClientRegistrationId();
	        
	      
	        if(registrationId.equals("google")) {
	        	 username = (String) oauth2User.getAttribute("email");
				 fulname = (String) oauth2User.getAttributes().get("name");
				 email = (String) oauth2User.getAttributes().get("email");														
				 pictureUrl = (String) oauth2User.getAttributes().get("picture"); 
				System.out.println("Đăng nhập Google thành công");
	        }else if (registrationId.equals("facebook")) {
		         username =  processString((String) oauth2User.getAttribute("name"));
				 fulname = (String) oauth2User.getAttributes().get("name");
				System.out.println("Đăng nhập Facebook thành công");
	        }
		
			UserDetails user = User.withUsername(username).password("123").roles("USER").build();

	        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());

	        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
	        
	        
	        account.setUsername(email != null ? email : username);
	        account.setFullName(fulname);
	        account.setEmail(email);
	        account.setImage(pictureUrl);
	        account.setRole(newAuthentication.toString().contains("USER") ? false : true);
	        account.setCreateDate(new Date());
	        accountDAO.save(account);
	        
			cookieService.setCookie(response, "username", username.replaceAll("\\s", ""), 3600);
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
	
	
	public static String processString(String input) {
        // Loại bỏ các kí tự không mong muốn và chuyển sang Unicode normalization (không dấu)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noDiacritics = pattern.matcher(normalized).replaceAll("");

        // Loại bỏ khoảng trắng và chuyển chuỗi thành dạng in hoa
        String result = noDiacritics.replaceAll("\\s+", "").toUpperCase();

        return result;
    }
}