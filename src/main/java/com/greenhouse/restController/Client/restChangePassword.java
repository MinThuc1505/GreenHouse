package com.greenhouse.restController.Client;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.model.Account;

@RestController
@RequestMapping(value = "/client/rest/change-password")
public class restChangePassword {
    @Autowired
    AccountDAO accountDao;

    @GetMapping(value = "/{username}")
    private ResponseEntity<Account> getOne(@PathVariable("username") String username) {
        if (!accountDao.existsById(username)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accountDao.findById(username).get());
    }

    @PatchMapping(value = "/{username}")
    private ResponseEntity<?> update(@PathVariable("username") String username,
            @RequestBody Map<String, Object> updates) {
        Optional<Account> optionalAccount = accountDao.findById(username);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Account account = optionalAccount.get();

        if (updates.containsKey("password")) {
            String encodedPassword = passwordEncoder.encode(account.getPassword());
            account.setPassword(encodedPassword);
        }

        Account updatedAccount = accountDao.save(account);
        return ResponseEntity.ok(updatedAccount);
    }
}
