package com.greenhouse.controller.Client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.DAO.CartDAO;
import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Message;
import com.greenhouse.model.Product;
import com.greenhouse.service.SessionService;

@Controller
@RequestMapping("/client/order")
public class OderController {

}
