package com.greenhouse.controller.Client;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Message;
import com.greenhouse.service.EncodeService;
import com.greenhouse.service.SessionService;
import com.greenhouse.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/client/change-password")
public class ChangePasswordController {
   
}
