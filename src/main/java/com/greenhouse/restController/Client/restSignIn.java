package com.greenhouse.restController.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.DTO.AccountDto;
import com.greenhouse.model.Account;

@RestController
@RequestMapping(value = "/client/rest/signIn")
public class restSignIn {

    @Autowired
    private AccountDAO accountDAO;

    @PostMapping
    public ResponseEntity<Map<String, Object>> login(@RequestBody AccountDto accountDto) {

        Account acc = new Account();
        acc = accountDAO.findByUsernameAndPassword(accountDto.getUsername(), accountDto.getPassword());
        Map<String, Object> responseData = new HashMap<>();
        if (acc != null) {
            System.out.println(">>>>>>>>>>>>>>>> Login success!");
            responseData.put("status", "success");
            responseData.put("message", "Login successful");
            responseData.put("username", accountDto.getUsername());
            return ResponseEntity.ok(responseData);
        }

        // Nếu thông tin đăng nhập không hợp lệ, trả về lỗi:
        responseData.put("status", "fail");
        responseData.put("message", "Login failed. Please check your username and password.");
        return ResponseEntity.ok(responseData);
    }

}
