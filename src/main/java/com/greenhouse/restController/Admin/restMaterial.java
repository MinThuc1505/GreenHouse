package com.greenhouse.restController.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.MaterialDAO;
import com.greenhouse.model.Material;

@RestController
@RequestMapping(value = "/rest/materials")
public class restMaterial {
    
    @Autowired
    MaterialDAO materialDAO;
     @GetMapping
    private ResponseEntity<List<Material>> getAllAccounts(){
        return ResponseEntity.ok(materialDAO.findAll());
    }
     @GetMapping(value = "/{id}")
    private ResponseEntity<Material> getOne(@PathVariable("id") Integer id) {
        if (!materialDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(materialDAO.findById(id).get());
    }

    @PostMapping
    private ResponseEntity<?> create(@RequestBody Material material) {
        System.out.println(material);
    
        // Kiểm tra tên chất liệu đã tồn tại hay chưa
        Material existingMaterial = materialDAO.findByMaterial(material.getMaterial());
        if (existingMaterial != null) {
            return ResponseEntity.badRequest().build();
        }
    
        if (material.getId() != null && materialDAO.existsById(material.getId())) {
            return ResponseEntity.badRequest().build();
        }
    
        return ResponseEntity.ok(materialDAO.save(material));
    }
    
}
