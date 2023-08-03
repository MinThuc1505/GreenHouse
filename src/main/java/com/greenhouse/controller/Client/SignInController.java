package com.greenhouse.controller.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.config.SecurityConfig;
import com.greenhouse.model.Account;
import com.greenhouse.model.Message;
import com.greenhouse.service.CookieService;
import com.greenhouse.service.SessionService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;

@Controller
public class SignInController {
	@Autowired
	private CookieService cookieService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private AccountDAO accountDAO;
	@Value("${recaptcha.secret}")
	private String recapchaSecret;
	@Value("${recaptcha.url}")
	private String recapchaUrl;

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

}