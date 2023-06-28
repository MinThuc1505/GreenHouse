package com.greenhouse.controller.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.model.Account;
import com.greenhouse.service.SessionService;

@Controller
@RequestMapping("/client/blog")
public class BlogController {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private AccountDAO accountDAO;

    @GetMapping("")
    public String blog(Model model) {
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
        model.addAttribute("template", "blog.html");
        model.addAttribute("fragment", "content");
        return "client/main-layout";
    }
}
