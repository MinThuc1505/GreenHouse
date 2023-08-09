package com.greenhouse.restController.Admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.model.Account;

import io.micrometer.common.util.StringUtils;


@RestController
@RequestMapping(value = "/rest/user")
public class restUser {
    
    @Autowired
    AccountDAO accountDAO;

    @GetMapping
    private ResponseEntity<List<Account>> getAllAccounts(){
        return ResponseEntity.ok(accountDAO.findAll());
    }

    @GetMapping(value = "/{username}")
    private ResponseEntity<Account> getOne(@PathVariable("username") String username){
        if(!accountDAO.existsById(username)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accountDAO.findById(username).get());
    }

   @PostMapping
    private ResponseEntity<?> create(@RequestBody Account account) {

        Map<String, String> errors = validateAccount(account);

        if (account.getUsername() != null && errors.isEmpty()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(account.getPassword());
            account.setPassword(encodedPassword);
            Account createdAccount = accountDAO.save(account);
            return ResponseEntity.ok(createdAccount);
        } else if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        } else {
            errors.put("otherError", "Thông tin tài khoản không hợp lệ.");
            return ResponseEntity.badRequest().body(errors);
        }

    }
    

    @PutMapping(value = "/{username}")
    private ResponseEntity<Account> update (@PathVariable("username") String username, @RequestBody Account account){
    	
    	System.out.println( account);
    	
        if (!accountDAO.existsById(username)) {
            return ResponseEntity.notFound().build();
            
        }
        return ResponseEntity.ok(accountDAO.save(account));
    }

    @DeleteMapping(value = "/{username}")
    private ResponseEntity <Void> delete(@PathVariable("username") String username){
        if(!accountDAO.existsById(username)){
            return ResponseEntity.notFound().build();
        }
        accountDAO.deleteById(username);
        return ResponseEntity.ok().build();
    } 

    @GetMapping(value = "/search")
    public ResponseEntity<Account> searchData(@RequestParam("username") String username) {
        Account searchResult = accountDAO.findById(username).orElse(null);

        if (searchResult == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(searchResult);
    }


    private Map<String, String> validateAccount(Account account) {
        Map<String, String> errors = new HashMap<>();

        if (StringUtils.isEmpty(account.getUsername())) {
            errors.put("username", "Tên tài khoản không bỏ trống.");
        }else if (!isValidUsername(account.getUsername())) {
            errors.put("username", "Tên tài khoản chỉ chứa chữ cái hoặc chữ số.");
        }

        if (StringUtils.isEmpty(account.getPassword())) {
            errors.put("password", "Mật khẩu không bỏ trống.");
        }else if (!isValidPassword(account.getPassword())) {
            errors.put("password", "Mật khẩu chứa ít nhất một chữ cái hoa, một chữ cái thường, một chữ số và một ký tự đặt biệt");
        }

        if (StringUtils.isEmpty(account.getFullName())) {
            errors.put("fullName", "Họ và tên không bỏ trống.");
        }
        if (StringUtils.isEmpty(account.getAddress())) {
            errors.put("address", "Địa chỉ không bỏ trống.");
        }
        if (account.getEmail() == null || !isValidEmail(account.getEmail())) {
            errors.put("email", "Định dạng email không hợp lệ.");
        } else if (accountDAO.existsByEmail(account.getEmail())) {
            errors.put("emailExists", "Email đã được đăng ký.");
        }

        if (account.getPhone() == null || !isValidPhoneNumber(account.getPhone())) {
            errors.put("phone", "Định dạng số điện thoại không hợp lệ.");
        }else if (accountDAO.existsByPhone(account.getPhone())) {
            errors.put("phoneExists", "Số điện thoại đã được đăng ký.");
        }

        if (accountDAO.existsById(account.getUsername())) {
            errors.put("AccountExists", "Tài khoản đã được đăng ký.");
        }
    // thêm dịa chỉ nữa code đi
        return errors;
    }

    private boolean isValidEmail(String email) {
        // Xác thực định dạng email đơn giản
        String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        return email.matches(emailPattern);
    }

    private boolean isValidPhoneNumber(String phone) {
        // Xác thực định dạng số điện thoại tiếng Việt
        String phonePattern = "^(\\+?84|0)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-9|6|8|9]|9[0-3|5-9])[0-9]{7}$";
        return phone.matches(phonePattern);
    }

    private boolean isValidUsername(String username) {
        // Kiểm tra định dạng tên tài khoản, ví dụ: chỉ chứa chữ cái, chữ số và ký tự đặc biệt
        String usernamePattern = "^[A-Za-z0-9_.-]+$";
        return username.matches(usernamePattern);
    }
    
    private boolean isValidPassword(String password) {
        // Kiểm tra độ phức tạp mật khẩu, ví dụ: chứa ít nhất một chữ cái hoa, một chữ cái thường, một chữ số và một ký tự đặc biệt
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordPattern);
    }
}