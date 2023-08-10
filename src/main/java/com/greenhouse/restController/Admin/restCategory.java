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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.CategoryDAO;
import com.greenhouse.DAO.ProviderDAO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Category;
import com.greenhouse.model.Provider;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping(value = "/rest/category")
public class restCategory {
    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    public restCategory(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @GetMapping
    private ResponseEntity<List<Category>> getAllCategory() {
        return ResponseEntity.ok(categoryDAO.findAll());
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<Category> getOne(@PathVariable("id") String id) {
        if (!categoryDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoryDAO.findById(id).get());
    }

    @PostMapping
    private ResponseEntity<?> create(@RequestBody Category category) {
        Map<String, String> errors = validateCategory(category);
        if (category != null && errors.isEmpty()) {
            Category createdCategory = categoryDAO.save(category);
            return ResponseEntity.ok(createdCategory);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @PutMapping(value = "/{id}")
    private ResponseEntity<Category> update(@PathVariable("id") String id, @RequestBody Category category) {
        if (!categoryDAO.existsById(id)) {
            return ResponseEntity.notFound().build();

        }
        return ResponseEntity.ok(categoryDAO.save(category));
    }

    @DeleteMapping(value = "/{id}")
    private ResponseEntity<Void> delete(@PathVariable("id") String id) {
        if (!categoryDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoryDAO.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Category> searchData(@RequestParam("name") String name) {
        Category searchResult = categoryDAO.findByName(name);

        if (searchResult == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(searchResult);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Category>> getAllAccounts(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        try {
            System.out.println("Requested Page: " + page);
            System.out.println("Page Size: " + size);

            Pageable pageable = PageRequest.of(page, size);
            Page<Category> categories = categoryDAO.findAll(pageable);

            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            // Xử lý các exception nếu cần thiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Map<String, String> validateCategory(Category category) {
        Map<String, String> errors = new HashMap<>();

        if (StringUtils.isEmpty(category.getName())) {
            errors.put("name", "Tên thể loại không bỏ trống.");
        }
        if (category.getId() == null || !isValidId(category.getId())) {
            errors.put("email", "Định dạng mã loại sản phẩm không hợp lệ.");
        } else if (categoryDAO.existsById(category.getId())) {
            errors.put("idExists", "Mã loại sản phẩm đã tồn tại.");
        }

        if (categoryDAO.existsById(category.getId())) {
            errors.put("CategoryExists", "Thể loại này đã tồn tại.");
        }
        // thêm dịa chỉ nữa code đi
        return errors;
    }

    private boolean isValidId(String id) {
        // Kiểm tra định dạng id, chỉ chứa kí tự in hoa
        String idePattern = "^[A-Z]+$";
        return id.matches(idePattern);
    }
}
