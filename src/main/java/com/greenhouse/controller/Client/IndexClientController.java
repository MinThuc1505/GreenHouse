package com.greenhouse.controller.Client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/client/index")
public class IndexClientController {
   
	
	@GetMapping("")
	public String signin() {
		
		return "client/layouts/index";
	}
   
}
