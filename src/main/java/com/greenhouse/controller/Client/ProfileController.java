package com.greenhouse.controller.Client;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.model.Account;
import com.greenhouse.service.SessionService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/client/profile")
public class ProfileController {
	
}
