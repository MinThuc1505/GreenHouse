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
    @Autowired
    private SessionService sessionService;
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private AccountDAO accountDAO;

    @GetMapping("/index")
    public String index(Model model) {
        // TOP 10 SẢN PHẨM GIẢM GIÁ
        Pageable pageable = PageRequest.of(0, 10);
        List<Object[]> top10Products = productDAO.findTop10ProductsWithDiscounts(pageable);

        model.addAttribute("products", top10Products);
        // User Session - start
        Account account = sessionService.get("account");
        if (account != null) {
            try {
                int qty = accountDAO.findQuanityCartById(account.getId());
                model.addAttribute("qtyCart", qty);
            } catch (Exception e) {
                model.addAttribute("qtyCart", 0);
            }
            model.addAttribute("sessionUsername", account.getUsername());
        }
        // User Session - end
        model.addAttribute("message", Message.message);
        model.addAttribute("typeMessage", Message.type);
        Message.message = "";
        return "client/layouts/index";
    }

    @RequestMapping("/contact")
    public String contact(Model model) {
        return "client/layouts/contact";
    }

   
}
