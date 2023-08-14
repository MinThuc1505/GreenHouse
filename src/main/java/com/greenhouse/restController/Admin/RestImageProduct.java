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

import com.greenhouse.DAO.ImageProductDAO;
import com.greenhouse.model.Discount;
import com.greenhouse.model.ImageProduct;

@RestController
@RequestMapping(value = "/rest/imageProducts")
public class RestImageProduct {
    @Autowired
    ImageProductDAO imageProductDAO;

    @GetMapping
    private ResponseEntity<List<ImageProduct>> getAllPriceHistorys() {
        return ResponseEntity.ok(imageProductDAO.findAll());
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<ImageProduct> getOne(@PathVariable("id") Integer id) {
        if (!imageProductDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imageProductDAO.findById(id).get());
    }

    @PostMapping
    private ResponseEntity<ImageProduct> create(@RequestBody ImageProduct imageProduct) {
        if ( imageProduct.getId() != null && imageProductDAO.existsById(imageProduct.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(imageProductDAO.save(imageProduct));
    }

    @PutMapping(value = "/{id}")
    private ResponseEntity<ImageProduct> update(@PathVariable("id") Integer id,
            @RequestBody ImageProduct imageProduct) {

        System.out.println(id);

        if (!imageProductDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imageProductDAO.save(imageProduct));
    }

    @DeleteMapping(value = "/{id}")
    private ResponseEntity<ImageProduct> delete(@PathVariable("id") Integer id) {
        if (!imageProductDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        imageProductDAO.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
