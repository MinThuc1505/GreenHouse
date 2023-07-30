package com.greenhouse.controller.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.DAO.CartDAO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Cart;
import com.greenhouse.model.Message;
import com.greenhouse.service.SessionService;

@Controller
@RequestMapping("/client/cart")
public class CartController {

}
