package com.greenhouse.restController.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.greenhouse.model.Category;
import com.greenhouse.model.Provider;

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
    private ResponseEntity<Category> create(@RequestBody Category category) {
        System.out.println(category);
        if (categoryDAO.existsById(category.getId())) {
            return ResponseEntity.badRequest().build();

        }
        return ResponseEntity.ok(categoryDAO.save(category));
    }

    @PutMapping(value = "/{id}")
    private ResponseEntity<Category> update (@PathVariable("id") String id, @RequestBody Category category){
        if (!categoryDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
            
        }
        return ResponseEntity.ok(categoryDAO.save(category));
    }

    @DeleteMapping(value = "/{id}")
    private ResponseEntity <Void> delete(@PathVariable("id") String id){
        if(!categoryDAO.existsById(id)){
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
}
