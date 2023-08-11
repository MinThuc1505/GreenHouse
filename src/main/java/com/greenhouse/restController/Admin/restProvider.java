package com.greenhouse.restController.Admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

import com.greenhouse.DAO.ProviderDAO;
import com.greenhouse.model.Discount;
import com.greenhouse.model.Provider;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping(value = "/rest/provider")
public class restProvider {
    @Autowired
    ProviderDAO providerDAO;

    @Autowired
    public restProvider(ProviderDAO providerDAO) {
        this.providerDAO = providerDAO;
    }

    @GetMapping
    private ResponseEntity<List<Provider>> getAllProvider() {
        return ResponseEntity.ok(providerDAO.findAll());
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<Provider> getOne(@PathVariable("id") String id) {
        if (!providerDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(providerDAO.findById(id).get());
    }

   @PostMapping
    private ResponseEntity<?> create(@RequestBody Provider provider) {

        Map<String, String> errors = validateProvider(provider);
        if (provider != null && errors.isEmpty()) {

            Provider createdProvider = providerDAO.save(provider);
            return ResponseEntity.ok(createdProvider);
        }
            return ResponseEntity.badRequest().body(errors);
    }

    @PutMapping(value = "/{id}")
    private ResponseEntity<Provider> update (@PathVariable("id") String id, @RequestBody Provider provider){
        if (!providerDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
            
        }
        return ResponseEntity.ok(providerDAO.save(provider));
    }

    @DeleteMapping(value = "/{id}")
    private ResponseEntity <Void> delete(@PathVariable("id") String id){
        if(!providerDAO.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        providerDAO.deleteById(id);
        return ResponseEntity.ok().build();
    } 

    @GetMapping(value = "/search")
    public ResponseEntity<Provider> searchData(@RequestParam("name") String name) {
        Provider searchResult = providerDAO.findByName(name);

        if (searchResult == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(searchResult);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Provider>> getAllAccounts(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        try {
            System.out.println("Requested Page: " + page);
            System.out.println("Page Size: " + size);

            Pageable pageable = PageRequest.of(page, size);
            Page<Provider> providers = providerDAO.findAll(pageable);

            return ResponseEntity.ok(providers);
        } catch (Exception e) {
            // Xử lý các exception nếu cần thiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    private Map<String, String> validateProvider(Provider provider) {
        Map<String, String> errors = new HashMap<>();

        if (StringUtils.isEmpty(provider.getName())) {
            errors.put("name", "Tên nhà cung cấp không bỏ trống.");
        }

        if (StringUtils.isEmpty(provider.getAddress())) {
            errors.put("address", "Địa chỉ không bỏ trống.");
        }
        if (provider.getEmail() == null || !isValidEmail(provider.getEmail())) {
            errors.put("email", "Định dạng email không hợp lệ.");
        } else if (providerDAO.existsByEmail(provider.getEmail())) {
            errors.put("emailExists", "Email đã được đăng ký.");
        }

        if (provider.getPhone() == null || !isValidPhoneNumber(provider.getPhone())) {
            errors.put("phone", "Định dạng số điện thoại không hợp lệ.");
        }else if (providerDAO.existsByPhone(provider.getPhone())) {
            errors.put("phoneExists", "Số điện thoại đã được đăng ký.");
        }

        if (providerDAO.existsById(provider.getId())) {
            errors.put("ProviderExists", "Tài khoản đã được đăng ký.");
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
}
