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

    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SessionService sessionService;

    @GetMapping("")
    public String forgotPassword(Model model, Account account) {
        // User Session - start
        Account acc = sessionService.get("account");
        if (acc != null) {
            try {
                int qty = accountDAO.findQuanityCartById(acc.getId());
                model.addAttribute("qtyCart", qty);
            } catch (Exception e) {
                model.addAttribute("qtyCart", 0);
            }
            model.addAttribute("sessionUsername", acc.getUsername());
        }
        // User Session - end
        model.addAttribute("account", account);
        return "client/layouts/forgot-password";
    }

    @PostMapping("/sendmail")
    public String sendmail(@ModelAttribute("account") Account account,
            @RequestParam("email") String email,
            HttpServletRequest request,
            Model model) {
        Account acc = accountDAO.checkDuplicateEmail(email);
        if (acc != null) {
            // Gửi mail kích hoạt tài khoản
            String activationToken = tokenService.generateToken(acc.getEmail());
            model.addAttribute("message1", "Kiểm tra email của bạn.");
            // Tạo đường link kích hoạt tài khoản
            String activationLink = request.getScheme() + "://" + request.getServerName() + ":"
                    + request.getServerPort() + request.getContextPath() + "/client/change-password?id="
                    + acc.getId() + "&token=" + activationToken;
            emailService.sendEmailForgotPassword(acc.getEmail(), "Yêu cầu đặt lại mật khẩu GreenHouse", activationLink);
            model.addAttribute("message",
                    "Yêu cầu đặt lại mật khẩu đã được gửi tới email của bạn, hãy kiểm tra để đặt lại mật khẩu.");
            model.addAttribute("typeMessage", "success");
        }
        model.addAttribute("template", "forgot-password.html");
        model.addAttribute("fragment", "content");
        return "client/main-layout";
    }
}
