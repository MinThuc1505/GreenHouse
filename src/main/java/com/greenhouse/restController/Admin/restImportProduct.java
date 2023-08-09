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

import com.greenhouse.DAO.ImportProductDAO;
import com.greenhouse.model.ImportProduct;

@RestController
@RequestMapping(value = "/rest/importProducts")
public class restImportProduct {

    @Autowired
    ImportProductDAO importProductDAO;

    @GetMapping
    private ResponseEntity<List<ImportProduct>> getAllImportProducts(){
        return ResponseEntity.ok(importProductDAO.findAll());
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<ImportProduct> getOne(@PathVariable("id") Integer id){
        if (!importProductDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(importProductDAO.findById(id).get());
    }

    @PostMapping
    private ResponseEntity<ImportProduct> create(@RequestBody ImportProduct importProduct){
        if (importProductDAO.existsById(importProduct.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(importProductDAO.save(importProduct));
    }

    @PutMapping(value = "/{id}")
    private ResponseEntity<ImportProduct> update(@PathVariable("id") Integer id, @RequestBody ImportProduct importProduct){
        if (!importProductDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        importProduct.setId(id);
        return ResponseEntity.ok(importProductDAO.save(importProduct));
    }

    @DeleteMapping(value = "/{id}")
    private ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        if (!importProductDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        importProductDAO.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
