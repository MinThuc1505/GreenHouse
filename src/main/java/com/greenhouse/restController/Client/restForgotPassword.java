package com.greenhouse.restController.Client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.model.Account;
import com.greenhouse.service.EmailService;
import com.greenhouse.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/client/rest/forgot-password")
public class restForgotPassword {

    @Autowired
    private EmailService emailService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    HttpServletRequest request;

    @PostMapping("/sendmail")
    public ResponseEntity<Map<String, String>> sendResetPasswordEmail(
            @RequestBody Map<String, String> requestData) {
        Map<String, String> responseData = new HashMap<>();
        String status = "";
        String message = "";
        try {
            String email = requestData.get("email");
            if (email != null) {
                // Gửi mail kích hoạt tài khoản
                Account acc = accountDAO.findByEmail(email);
                System.out.println(acc);
                if (acc != null) {
                    String activationToken = tokenService.generateTokenByUsername(acc.getUsername());
                    String activationLink = generateActivationLink(request, activationToken);
                    emailService.sendEmailForgotPassword(acc.getEmail(), activationLink);
                    status = "success";
                    message = "Đã gửi yêu cầu đặt lại mật khẩu đến email của bạn!";
                } else {
                    status = "error";
                    message = "Email này chưa đăng ký tài khoản!";
                }
            } else {
                status = "error";
                message = "Hãy nhập email cần đặt lại mật khẩu!";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }

    private String generateActivationLink(HttpServletRequest request, String activationToken) {
        return request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + request.getContextPath() + "/client/change-password?&token="
                + activationToken;
    }
}
