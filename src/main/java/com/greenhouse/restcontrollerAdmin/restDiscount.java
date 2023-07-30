package com.greenhouse.restcontrollerAdmin;

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

import com.greenhouse.DAO.DiscountDAO;
import com.greenhouse.model.Discount;


@RestController
@RequestMapping(value = "/rest/discounts")
public class restDiscount {

	@Autowired
	DiscountDAO discountDAO;
	
	@GetMapping
    private ResponseEntity<List<Discount>> getAllAccounts(){
        return ResponseEntity.ok(discountDAO.findAll());
    }
	
	@GetMapping(value = "/{discount_code}")
    private ResponseEntity<Discount> getOne(@PathVariable("discount_code") String discount_code){
        if(!discountDAO.existsById(discount_code)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(discountDAO.findById(discount_code).get());
    }

    @PostMapping
    private ResponseEntity<Discount> create(@RequestBody Discount discount){
    	
    	System.out.println(discount);
    	
        if(discountDAO.existsById(discount.getDiscount_code())){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(discountDAO.save(discount));
    }

    @PutMapping(value = "/{discount_code}")
    private ResponseEntity<Discount> update(@PathVariable("discount_code") String discount_code,@RequestBody Discount discount){
        if(!discountDAO.existsById(discount_code)){
            return ResponseEntity.notFound().build();
        }
         return ResponseEntity.ok(discountDAO.save(discount)); 
    }

    @DeleteMapping(value = "/{discount_code}")
    private ResponseEntity<Void> delete(@PathVariable("discount_code") String discount_code){
         if(!discountDAO.existsById(discount_code)){
            return ResponseEntity.notFound().build();
        }
         discountDAO.deleteById(discount_code);
        return ResponseEntity.ok().build(); 
    }
}
