package com.greenhouse.controller.Admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenhouse.DAO.CategoryDAO;
import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.DAO.SetCategoryDAO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Category;
import com.greenhouse.model.Product;
import com.greenhouse.model.SetCategory;
import com.greenhouse.service.SessionService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("admin")
public class CategoryController {
	

}