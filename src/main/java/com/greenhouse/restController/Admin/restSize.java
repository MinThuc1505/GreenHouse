package com.greenhouse.restcontrollerAdmin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.SizeDAO;
import com.greenhouse.model.Size;


@RestController
@RequestMapping(value = "/rest/sizes")
public class restSize {
    
    @Autowired
    SizeDAO sizeDAO;
     @GetMapping
    private ResponseEntity<List<Size>> getAllAccounts(){
        return ResponseEntity.ok(sizeDAO.findAll());
    }
}
