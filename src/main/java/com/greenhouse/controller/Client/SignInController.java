package com.greenhouse.controller.Client;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Message;
import com.greenhouse.model.ReCapchaResponse;
import com.greenhouse.service.CookieService;
import com.greenhouse.service.EncodeService;
import com.greenhouse.service.SessionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/client/signin")
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
	
	@GetMapping
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