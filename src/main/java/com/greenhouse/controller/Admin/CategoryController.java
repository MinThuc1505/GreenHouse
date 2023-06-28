package com.greenhouse.controller.Admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    CategoryDAO dao;
    @Autowired
    SetCategoryDAO setCategoryDAO;

    @Autowired
    ProductDAO productDAO;
    @Autowired
    private SessionService sessionService;

    @RequestMapping("categories")
    public String category(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        int pageSize = 5; // Số lượng mục trên mỗi trang
        Page<Category> categoryPage = getPageableCategories(page, pageSize);
        List<Category> categories = categoryPage.getContent();
        int totalPage = categoryPage.getTotalPages();
        int currentPage = categoryPage.getNumber();
        int[] startAndEndPage = calculateStartAndEndPage(totalPage, currentPage);
        setCommonModelAttributes(model, categories, totalPage, currentPage, startAndEndPage);
        model.addAttribute("category", new Category());
        model.addAttribute("categories", categories);
        model.addAttribute("template", "categories.html");
        model.addAttribute("fragment", "content");

        return "admin/main-layout";
    }

  @PostMapping("/categories/save")
public String saveCategory(@Valid @ModelAttribute("category") Category category, BindingResult result, Model model,
        RedirectAttributes redirectAttributes,
        @RequestParam(value = "page", defaultValue = "0") int page) {

    if (result.hasErrors()) {
        List<Category> categories = getCategoryList(page, 5);

        int totalPage = calculateTotalPage(page, 5);
        int[] startAndEndPage = calculateStartAndEndPage(totalPage, page);
        setCommonModelAttributes(model, categories, totalPage, page, startAndEndPage);
        model.addAttribute("template", "categories.html");
        model.addAttribute("fragment", "content");
        return "admin/main-layout";
    }

    // Kiểm tra trùng lặp dựa vào id
    Category existingCategory = dao.findById(category.getId()).orElse(null);
    if (existingCategory != null) {
        redirectAttributes.addFlashAttribute("errorMessage", "ID đã tồn tại!");
        return "redirect:/admin/categories?page=" + page;
    }

    // Kiểm tra trùng lặp dựa vào categoryName
    Category existingCategoryName = dao.findByCategoryName(category.getCategoryName());
    if (existingCategoryName != null) {
        redirectAttributes.addFlashAttribute("errorMessage", "Tên thể loại đã tồn tại!");
        return "redirect:/admin/categories?page=" + page;
    }

    dao.save(category);
    redirectAttributes.addFlashAttribute("successMessage", "Thêm thành công!");

    return "redirect:/admin/categories?page=" + page;
}

@PostMapping("/categories/update")
public String updateCategory(@Valid @ModelAttribute("category") Category category, BindingResult result,
        RedirectAttributes redirectAttributes,
        Model model,
        @RequestParam(value = "page", defaultValue = "0") int page) {
    if (result.hasErrors()) {
        List<Category> categories = getCategoryList(page, 5);

        int totalPage = calculateTotalPage(page, 5);
        int[] startAndEndPage = calculateStartAndEndPage(totalPage, page);
        setCommonModelAttributes(model, categories, totalPage, page, startAndEndPage);
        model.addAttribute("template", "categories.html");
        model.addAttribute("fragment", "content");
        return "admin/main-layout";
    }

    // Kiểm tra trùng lặp dựa vào categoryName
    Category existingCategoryName = dao.findByCategoryName(category.getCategoryName());
    if (existingCategoryName != null && !existingCategoryName.getId().equals(category.getId())) {
        redirectAttributes.addFlashAttribute("errorMessage", "Tên thể loại đã tồn tại!");
        return "redirect:/admin/categories?pages=" + page;
    }

    dao.save(category);
    redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công!");

    return "redirect:/admin/categories?pages=" + page;
}

    @PostMapping("/categories/delete")
    public String deleteCategory(@ModelAttribute("category") Category category, RedirectAttributes redirectAttributes,
            @RequestParam(value = "page", defaultValue = "0") int page) {

                  if (!dao.existsById(category.getId())) {
         redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa!");
       return "redirect:/admin/categories?page=" + page;
    }
        try {
            dao.delete(category);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thể Loại đang được sử dụng!");
        }

        return "redirect:/admin/categories?page=" + page;
    }

    @GetMapping("/categories/edit")
    public String edit(@RequestParam("id") String id, @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {
        Optional<Category> category = dao.findById(id);
        List<Category> categories = getCategoryList(page, 5);

        int totalPage = calculateTotalPage(page, 5);
        int[] startAndEndPage = calculateStartAndEndPage(totalPage, page);
        setCommonModelAttributes(model, categories, totalPage, page, startAndEndPage);
        model.addAttribute("category", category);
        model.addAttribute("template", "categories.html");
        model.addAttribute("fragment", "content");

        return "admin/main-layout";
    }

    private Page<Category> getPageableCategories(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return dao.findAll(pageable);
    }

    private List<Category> getCategoryList(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Category> categoryPage = dao.findAll(pageable);
        return categoryPage.getContent();
    }

    private int calculateTotalPage(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Category> categoryPage = dao.findAll(pageable);
        return categoryPage.getTotalPages();
    }

    private int[] calculateStartAndEndPage(int totalPage, int currentPage) {
        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPage - 1, currentPage + 2);
        return new int[] { startPage, endPage };
    }

    private Page<Category> searchCategoriesByKeyword(String keyword, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return dao.findByKeyword(keyword, pageable);
    }

    private void setCommonModelAttributes(Model model, List<Category> categories, int totalPage, int currentPage,
            int[] startAndEndPage) {
        Account account = sessionService.get("account");
        model.addAttribute("account", account);
        model.addAttribute("categories", categories);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startAndEndPage[0]);
        model.addAttribute("endPage", startAndEndPage[1]);
    }

    @GetMapping("/categories/sort")
    public String sortCategories(Model model, @RequestParam("field") String field,
            @RequestParam(value = "direct", defaultValue = "ASC") String direct,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        String sortDirection = direct.equalsIgnoreCase("ASC") ? "DESC" : "ASC";
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.fromString(direct), field));
        Page<Category> categoryPage = dao.findAll(pageable);

        int totalPage = categoryPage.getTotalPages();
        int currentPage = categoryPage.getNumber();
        int[] startAndEndPage = calculateStartAndEndPage(totalPage, currentPage);

        setCommonModelAttributes(model, categoryPage.getContent(), totalPage, currentPage, startAndEndPage);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("category", new Category());
        model.addAttribute("template", "categories.html");
        model.addAttribute("fragment", "content");

        return "admin/main-layout";
    }

    @GetMapping("/categories/search")
    public String searchCategories(Model model, @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        int pageSize = 5; // Số lượng mục trên mỗi trang
        Page<Category> categoryPage = searchCategoriesByKeyword(keyword, page, pageSize);

        List<Category> categories = categoryPage.getContent();
        int totalPage = categoryPage.getTotalPages();
        int currentPage = categoryPage.getNumber();
        int[] startAndEndPage = calculateStartAndEndPage(totalPage, currentPage);

        setCommonModelAttributes(model, categories, totalPage, currentPage, startAndEndPage);
        model.addAttribute("category", new Category());
        model.addAttribute("template", "categories.html");
        model.addAttribute("fragment", "content");
        model.addAttribute("keyword", keyword);

        return "admin/main-layout";
    }

    @RequestMapping("setcategories")
    public String setCategory(Model model, @RequestParam(value = "pages", defaultValue = "0") int pages) {
        int pageSize = 5; // Số lượng mục trên mỗi trang
        Page<SetCategory> setCategoryPage = getPageableSetCategories(pages, pageSize);
        List<SetCategory> setCategories = setCategoryPage.getContent();
        int totalPages = setCategoryPage.getTotalPages();
        int currentPages = setCategoryPage.getNumber();
        int[] startAndEndPages = calculateStartAndEndPages(totalPages, currentPages);
        List<Product> products = productDAO.findAll();
        model.addAttribute("products", products);
        model.addAttribute("setCategories", setCategories);
        model.addAttribute("setCategory", new SetCategory());
        setCommonsModelAttributes(model, setCategories, totalPages, currentPages, startAndEndPages);
        model.addAttribute("template", "categories.html");
        model.addAttribute("fragment", "content");

        return "admin/main-layout";
    }

    @PostMapping("/setcategories/save")
    public String saveSetCategory(@ModelAttribute("setCategory") SetCategory setCategory,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "pages", defaultValue = "0") int pages) {
        try {
            setCategoryDAO.save(setCategory);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lưu Thất bại!");
        }
        return "redirect:/admin/categories?pages=" + pages;
    }

    @PostMapping("/setcategories/delete")
    public String deleteSetCategory(@ModelAttribute("setCategory") SetCategory setCategory,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "pages", defaultValue = "0") int pages) {
        setCategoryDAO.delete(setCategory);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa thành công!");

        return "redirect:/admin/categories?pages=0";
    }

    @PostMapping("/setcategories/update")
    public String updateSetCategory(@ModelAttribute("setCategory") SetCategory setCategory,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "pages", defaultValue = "0") int pages) {
        setCategoryDAO.save(setCategory);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công!");

        return "redirect:/admin/categories?pages=" + pages;
    }

    @GetMapping("/setcategories/edit")
    public String editSetCategory(@RequestParam("id") Integer id,
            @RequestParam(value = "pages", defaultValue = "0") int pages,
            Model model) {
        Optional<SetCategory> setCategory = setCategoryDAO.findById(id);
        List<SetCategory> setcategories = getSetCategoryList(pages, 5);
        int totalPages = calculateTotalPages(pages, 5);
        int[] startAndEndPages = calculateStartAndEndPages(totalPages, pages);
        setCommonsModelAttributes(model, setcategories, totalPages, pages, startAndEndPages);
        model.addAttribute("setCategory", setCategory);
        model.addAttribute("template", "categories.html");
        model.addAttribute("fragment", "content");

        return "admin/main-layout";
    }

    private Page<SetCategory> getPageableSetCategories(int pages, int pageSize) {
        Pageable pageable = PageRequest.of(pages, pageSize);
        return setCategoryDAO.findAll(pageable);
    }

    private List<SetCategory> getSetCategoryList(int pages, int pageSize) {
        Pageable pageable = PageRequest.of(pages, pageSize);
        Page<SetCategory> categoryPage = setCategoryDAO.findAll(pageable);
        return categoryPage.getContent();
    }

    private int calculateTotalPages(int pages, int pageSize) {
        Pageable pageable = PageRequest.of(pages, pageSize);
        Page<Category> categoryPage = dao.findAll(pageable);
        return categoryPage.getTotalPages();
    }

    private int[] calculateStartAndEndPages(int totalPages, int currentPages) {
        int startPages = Math.max(0, currentPages - 2);
        int endPages = Math.min(totalPages - 1, currentPages + 2);
        return new int[] { startPages, endPages };
    }

    private void setCommonsModelAttributes(Model model, List<SetCategory> setCategories, int totalPages,
            int currentPages, int[] startAndEndPages) {
        Account account = sessionService.get("account");
        model.addAttribute("account", account);
        model.addAttribute("setCategories", setCategories);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPages", currentPages);
        model.addAttribute("startPages", startAndEndPages[0]);
        model.addAttribute("endPages", startAndEndPages[1]);
    }

}