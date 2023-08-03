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

import com.greenhouse.DAO.ProviderDAO;
import com.greenhouse.model.Provider;

@RestController
@RequestMapping(value = "/rest/provider")
public class restProvider {
    @Autowired
    ProviderDAO providerDAO;

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
    private ResponseEntity<Provider> create(@RequestBody Provider provider) {
        System.out.println(provider);
        if (providerDAO.existsById(provider.getId())) {
            return ResponseEntity.badRequest().build();

        }
        return ResponseEntity.ok(providerDAO.save(provider));
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
}
