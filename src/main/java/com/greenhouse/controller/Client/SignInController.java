package com.greenhouse.controller.Client;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
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

	@Autowired
	private RestTemplate restTemplate;

	private boolean verifyRecapcha(String recapcha) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("secret", recapchaSecret);
		map.add("response", recapcha);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		ReCapchaResponse response = restTemplate.postForObject(recapchaUrl, request, ReCapchaResponse.class);
		response.isSuccess();
		return response.isSuccess();
	}

	@GetMapping("")
	public String signin(Account account, Model model) {
		String username = cookieService.getValue("username");
		account.setUsername(username);
		model.addAttribute("message", Message.message);
		model.addAttribute("typeMessage", Message.type);
		Message.message = "";
		model.addAttribute("account", account);
		model.addAttribute("template", "signin.html");
		model.addAttribute("fragment", "content");
		return "client/main-layout";
	}

	@PostMapping("")
	private String checkLogin(@Valid @ModelAttribute("account") Account account, BindingResult result, Model model,
			@RequestParam("remember") Optional<Boolean> remember, HttpSession session, HttpServletRequest request) {

		if (result.hasFieldErrors("username") || result.hasFieldErrors("password")) {
			model.addAttribute("template", "signin.html");
			model.addAttribute("fragment", "content");
			return "client/main-layout";
		}

		// model.addAttribute("account", account);
		// String uri = sessionService.get("security-uri");
		// if (uri != null) {
		// return "redirect:" + uri;
		// }
		String username = account.getUsername().trim();
		String password = account.getPassword().trim();

		String gRecapcha = request.getParameter("g-recaptcha-response");
		if (!verifyRecapcha(gRecapcha)) {
			Message.message = "Hãy thực hiện captcha";
			Message.type = "error";
			model.addAttribute("message", Message.message);
			model.addAttribute("typeMessage", Message.type);
		} else {
			Account acc = accountDAO.checkDuplicateUsername(username);
			if (acc != null) {
				if (EncodeService.checkBcrypt(password, acc.getPassword())) {
					sessionService.set("account", acc);
					if (remember.orElse(false)) {
						cookieService.add("username", username, 24);
					} else {
						cookieService.remove("username");
					}
					// User Session - start
					try {
						int qty = accountDAO.findQuanityCartById(acc.getId());
						model.addAttribute("qtyCart", qty);
					} catch (Exception e) {
						model.addAttribute("qtyCart", 0);
					}
					model.addAttribute("sessionUsername", acc.getUsername());
					// User Session - end
					Message.message = "Đăng nhập thành công";
					Message.type = "success";
					model.addAttribute("message", Message.message);
					model.addAttribute("typeMessage", Message.type);
					if (acc.isRole()) {
						return "redirect:/admin/index";
					} else {
						return "redirect:/client/index";
					}
				} else {
					Message.message = "Tài khoản hoặc mật khẩu sai";
					Message.type = "error";
					model.addAttribute("message", Message.message);
					model.addAttribute("typeMessage", Message.type);
				}
			} else {
				Message.message = "Tài khoản hoặc mật khẩu sai";
				Message.type = "error";
				model.addAttribute("message", Message.message);
				model.addAttribute("typeMessage", Message.type);
			}
		}
		model.addAttribute("template", "signin.html");
		model.addAttribute("fragment", "content");
		return "client/main-layout";
	}

	@GetMapping("/compareEmail")
	public String compareEmail(Principal principal, Model model) {
		if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) principal;
			OAuth2User oauth2User = authenticationToken.getPrincipal();

			String googleEmail = (String) oauth2User.getAttribute("email");
			// String myEmail = "thuylam121002@gmail.com";

			Account account = accountDAO.checkDuplicateEmail(googleEmail);

			if (account != null) {

				sessionService.set("account", account);
				try {
					int qty = accountDAO.findQuanityCartById(account.getId());
					model.addAttribute("qtyCart", qty);
				} catch (Exception e) {
					model.addAttribute("qtyCart", 0);
				}
				model.addAttribute("sessionUsername", account.getUsername());
				Message.message = "Đăng nhập thành công!";
				Message.type = "success";
				model.addAttribute("message", Message.message);
				model.addAttribute("typeMessage", Message.type);
				if (account.isRole()) {
					return "redirect:/admin/index";
				} else {
					return "redirect:/client/index";
				}

			} else {
				Message.message = "Email chưa đăng ký tài khoản";
				Message.type = "error";
				model.addAttribute("message", Message.message);
				model.addAttribute("typeMessage", Message.type);
				return "redirect:/client/signin";
			}
		}
		model.addAttribute("template", "signin.html");
		model.addAttribute("fragment", "content");
		return "client/main-layout";
	}

}