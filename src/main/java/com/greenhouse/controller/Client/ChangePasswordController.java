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
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private SessionService sessionService;

    @GetMapping("")
    public String changePassword(Model model, @RequestParam("id") Optional<String> id,
            @RequestParam("token") Optional<String> token) {
        model.addAttribute("idAccount", id.orElse(null));
        model.addAttribute("token", token.orElse(null));
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
        model.addAttribute("message", Message.message);
        model.addAttribute("typeMessage", Message.type);
        Message.message = "";
        // User Session - end
        model.addAttribute("template", "change-password.html");
        model.addAttribute("fragment", "content");
        return "client/main-layout";
    }

    @PostMapping("")
    public String changePassword(Model model, @RequestParam("id") String id, HttpServletRequest request,
            @RequestParam("token") Optional<String> token, @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword) {
        Account account = sessionService.get("account");
        if (!tokenService.validateToken(token.get()) && account == null) {
            Message.message = "Hãy đăng nhập hoặc truy cập từ link trong email để đổi mật khẩu";
            Message.type = "error";
            return "redirect:/client/change-password";
        }
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$")) {
            Message.message = "Mật khẩu không chứa ký tự đặc biệt và phải bao gồm chữ in hoa, số và ít nhất 6 ký tự";
            Message.type = "error";
            return "redirect:/client/change-password";
        }
        if (!password.equals(confirmPassword)) {
            Message.message = "Mật khẩu không giống nhau";
            Message.type = "error";
            return "redirect:/client/change-password";
        }
        if (tokenService.validateToken(token.get())) {
            Account acc = accountDAO.findById(Integer.parseInt(id)).get();
            if (acc != null) {
                String encryptedPassword = EncodeService.hashBcrypt(password);
                acc.setPassword(encryptedPassword);
                accountDAO.save(acc);
                Message.message = "Đổi mật khẩu thành công";
                Message.type = "success";
                return "redirect:/client/signin";
            }
        } else if (account != null) {
            Account acc = accountDAO.findById(account.getId()).get();
            if (acc != null) {
                String encryptedPassword = EncodeService.hashBcrypt(password);
                acc.setPassword(encryptedPassword);
                accountDAO.save(acc);
                Message.message = "Đổi mật khẩu thành công";
                Message.type = "success";
                return "redirect:/client/logout";
            }
        }
        return "redirect:/client/change-password";
    }
}
