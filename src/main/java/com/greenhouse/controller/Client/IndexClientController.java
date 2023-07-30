package com.greenhouse.controller.Client;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Message;
import com.greenhouse.service.SessionService;

@Controller
@RequestMapping("/client")
public class IndexClientController {
   

   
}
