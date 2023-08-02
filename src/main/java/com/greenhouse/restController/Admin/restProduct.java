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
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.model.Product;

@RestController
@RequestMapping(value = "/rest/products")
public class restProduct {
    
    @Autowired
    ProductDAO productDAO;
     @GetMapping
    private ResponseEntity<List<Product>> getAllAccounts(){
        return ResponseEntity.ok(productDAO.findAll());
    }
     @GetMapping(value = "/{id}")
    private ResponseEntity<Product> getOne(@PathVariable("id") Long id){
        if(!productDAO.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDAO.findById(id).get());
    }
     @PostMapping
    private ResponseEntity<Product> create(@RequestBody Product product) {
        System.out.println(product);
        if (productDAO.existsById(product.getId())) {
            return ResponseEntity.badRequest().build();

        }
        return ResponseEntity.ok(productDAO.save(product));
    }

    @PutMapping(value = "/{id}")
    private ResponseEntity<Product> update (@PathVariable("id") Long id, @RequestBody Product product){
        if (!productDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
            
        }
        return ResponseEntity.ok(productDAO.save(product));
    }

    @DeleteMapping(value = "/{id}")
    private ResponseEntity <Void> delete(@PathVariable("id") Long id){
        if(!productDAO.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        productDAO.deleteById(id);
        return ResponseEntity.ok().build();
    } 

}
