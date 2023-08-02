package com.greenhouse.restcontrollerAdmin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
