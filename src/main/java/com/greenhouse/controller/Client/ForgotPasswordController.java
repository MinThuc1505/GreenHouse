package com.greenhouse.controller.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.model.Account;
import com.greenhouse.service.EmailService;
import com.greenhouse.service.SessionService;
import com.greenhouse.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/client/forgot-password")
public class ForgotPasswordController {

}
