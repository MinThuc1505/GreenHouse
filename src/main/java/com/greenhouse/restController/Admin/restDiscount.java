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
	
	@GetMapping(value = "/{discountCode}")
    private ResponseEntity<Discount> getOne(@PathVariable("discountCode") String discountCode){
        if(!discountDAO.existsById(discountCode)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(discountDAO.findById(discountCode).get());
    }

    @PostMapping
    private ResponseEntity<Discount> create(@RequestBody Discount discount){
        if(discountDAO.existsById(discount.getDiscountCode())){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(discountDAO.save(discount));
    }

    @PutMapping(value = "/{discountCode}")
    private ResponseEntity<Discount> update(@PathVariable("discountCode") String discountCode,@RequestBody Discount discount){
    	
    	System.out.println(discountCode);
    	
        if(!discountDAO.existsById(discountCode)){
            return ResponseEntity.notFound().build();
        }
         return ResponseEntity.ok(discountDAO.save(discount)); 
    }

    @DeleteMapping(value = "/{discountCode}")
    private ResponseEntity<Void> delete(@PathVariable("discountCode") String discountCode){
         if(!discountDAO.existsById(discountCode)){
            return ResponseEntity.notFound().build();
        }
         discountDAO.deleteById(discountCode);
        return ResponseEntity.ok().build(); 
    }
}
