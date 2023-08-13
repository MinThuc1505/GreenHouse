package com.greenhouse.restController.Client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.DAO.BillDAO;
import com.greenhouse.DAO.BillDetailDAO;
import com.greenhouse.model.Account;

@RestController
@RequestMapping(value = "/client/rest/profile")
public class restProfile {
    @Autowired
    AccountDAO accountDao;
    @Autowired
    private BillDAO billDao;
    @Autowired
    private BillDetailDAO detailDao;

    @GetMapping(value = "/{username}")
    private ResponseEntity<Map<String, Object>> getOne(@PathVariable("username") String username) {
        Map<String, Object> response = new HashMap<>();
        if (!accountDao.existsById(username)) {
            return ResponseEntity.notFound().build();
        }
        List<Object[]> bills = billDao.getBillsForClient(username);
        Account account = accountDao.findById(username).get();

        response.put("bills", bills);
        response.put("account", account);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/getBillDetails")
    private ResponseEntity<Map<String, Object>> getBillDetails(@RequestParam("billId") String billId) {
        Map<String, Object> response = new HashMap<>();

        List<Object[]> billDetails = detailDao.getBillDetailsForClient(billId);

        response.put("billDetails", billDetails);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{username}")
    private ResponseEntity<?> update(@PathVariable("username") String username,
            @RequestBody Map<String, Object> updates) {
        Optional<Account> optionalAccount = accountDao.findById(username);

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Account account = optionalAccount.get();

        // Áp dụng các thay đổi từ updates lên account (nếu có)
        if (updates.containsKey("fullName")) {
            account.setFullName((String) updates.get("fullName"));
        }

        if (updates.containsKey("email")) {
            account.setEmail((String) updates.get("email"));
        }

        if (updates.containsKey("phone")) {
            account.setPhone((String) updates.get("phone"));
        }

        if (updates.containsKey("address")) {
            account.setAddress(((String) updates.get("address")));
            ;
        }

        // Lưu account sau khi đã cập nhật
        Account updatedAccount = accountDao.save(account);
        return ResponseEntity.ok(updatedAccount);
    }
}
