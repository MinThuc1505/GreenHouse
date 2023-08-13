package com.greenhouse.restController.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.model.Account;
import com.greenhouse.service.TokenService;

@RestController
@RequestMapping(value = "/client/rest/change-password")
public class restChangePassword {
    @Autowired
    private AccountDAO accountDao;
    @Autowired
    private TokenService tokenService;

    @GetMapping(value = "/{username}")
    private ResponseEntity<Account> getOne(@PathVariable("username") String username) {
        if (!accountDao.existsById(username)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accountDao.findById(username).get());
    }

    @PatchMapping(value = "/{username}/{newpassword}")
    private ResponseEntity<?> update(@PathVariable("username") String username,@PathVariable("newpassword") String newpassword) {
        Optional<Account> optionalAccount = accountDao.findById(username);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Account account = optionalAccount.get();

        String encodedPassword = passwordEncoder.encode(newpassword);
        account.setPassword(encodedPassword);

        Account updatedAccount = accountDao.save(account);
        return ResponseEntity.ok(updatedAccount);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> resetPasswordFormEmail(
            @RequestParam String token, @RequestBody Map<String, Object> updates) {
        Map<String, String> responseData = new HashMap<>();
        String status = "";
        String message = "";
        if (tokenService.validateToken(token)) {
            System.out.println(token);
            String username = tokenService.getUsernameFromToken(token);
            System.out.println(username);
            Account acc = accountDao.findById(username).orElse(null);
            if (acc != null) {
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodePassword = passwordEncoder.encode(updates.get("password").toString());
                acc.setPassword(encodePassword);
                accountDao.save(acc);
                status = "success";
                message = "Đổi mật khẩu của tài khoản [" + username + "] thành công";
                tokenService.invalidateToken(token);
            }
        } else {
            status = "error";
            message = "Thời gian hiệu lực của mail đổi mật khẩu đã hết";
        }

        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }
}
