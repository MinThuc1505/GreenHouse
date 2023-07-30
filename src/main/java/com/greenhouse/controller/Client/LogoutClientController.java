package com.greenhouse.controller.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenhouse.model.Message;
import com.greenhouse.service.SessionService;

@Controller
@RequestMapping("/client/logout")
public class LogoutClientController {

}
