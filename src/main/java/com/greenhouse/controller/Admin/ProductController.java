package com.greenhouse.controller.Admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Product;
import com.greenhouse.service.SessionService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("admin/products")
public class ProductController {
    @Autowired
    private ProductDAO dao;
    @Autowired
    private SessionService sessionService;

    private String staticDir = System.getProperty("user.dir") + "/src/main/resources/static";

    @RequestMapping("")
    public String product(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        int pageSize = 5; // Số lượng mục trên mỗi trang
        Page<Product> productPage = getPageableProducts(page, pageSize);

        setProductPageInfo(model, productPage, page);

        model.addAttribute("product", new Product());

        return "admin/layouts/product";
    }

    private Page<Product> getPageableProducts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return dao.findAll(pageable);
    }

    private void setProductPageInfo(Model model, Page<Product> productPage, int page) {
        int totalPages = productPage.getTotalPages();
        int currentPage = productPage.getNumber();
        int[] startAndEndPages = calculateStartAndEndPages(totalPages, currentPage);
        Account account = sessionService.get("account");
        model.addAttribute("account", account);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startAndEndPages[0]);
        model.addAttribute("endPage", startAndEndPages[1]);
    }

    private int[] calculateStartAndEndPages(int totalPages, int currentPage) {
        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, currentPage + 2);
        return new int[] { startPage, endPage };
    }

    private void saveImage(MultipartFile image, String directory, String filename) throws IOException {
        Path targetDir = Path.of(directory + "/images");
        Files.createDirectories(targetDir); // Tạo thư mục nếu chưa tồn tại
        Path targetFile = targetDir.resolve(filename);
        Files.copy(image.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
    }

  @PostMapping("/save")
public String saveProduct(@Valid @ModelAttribute("product") Product product, BindingResult result, Model model,
        RedirectAttributes redirectAttributes,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam("tempImages") MultipartFile tempImages)
        throws IOException {

    // Kiểm tra nếu có lỗi trong dữ liệu nhập vào
    if (result.hasErrors()) {
        int pageSize = 5; // Số lượng mục trên mỗi trang
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> productPage = dao.findAll(pageable);

        setProductPageInfo(model, productPage, page);
        model.addAttribute("template", "products.html");
        model.addAttribute("fragment", "content");
        return "admin/main-layout";
    } else {
        // Kiểm tra trùng mã id
        if (dao.existsById(product.getId())) {
            result.rejectValue("id", "Product.id.duplicate", "Mã sản phẩm đã tồn tại");
            int pageSize = 5; // Số lượng mục trên mỗi trang
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Product> productPage = dao.findAll(pageable);

            setProductPageInfo(model, productPage, page);
            model.addAttribute("template", "products.html");
            model.addAttribute("fragment", "content");
            return "admin/main-layout";
        }

        if (tempImages.getOriginalFilename() != "") {
            saveImage(tempImages, staticDir, tempImages.getOriginalFilename());
            product.setImage(tempImages.getOriginalFilename());
        }

        dao.save(product);
        System.out.println("Thêm Ok");
        redirectAttributes.addFlashAttribute("successMessage", "thêm thành công!");
        return "redirect:/admin/products?page=" + page;
    }
}


   @PostMapping("/update")
public String updateProduct(@Valid @ModelAttribute("product") Product product, BindingResult result, Model model,
        RedirectAttributes redirectAttributes,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam("tempImages") MultipartFile tempImages)
        throws IOException {

    // Kiểm tra nếu có lỗi trong dữ liệu nhập vào
    if (result.hasErrors()) {
        int pageSize = 5; // Số lượng mục trên mỗi trang
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> productPage = dao.findAll(pageable);

        setProductPageInfo(model, productPage, page);
        model.addAttribute("template", "products.html");
        model.addAttribute("fragment", "content");
        return "admin/main-layout";
    } else {

        if (tempImages.getOriginalFilename() != "") {
            saveImage(tempImages, staticDir, tempImages.getOriginalFilename());
            product.setImage(tempImages.getOriginalFilename());
        }

        dao.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công!");

        return "redirect:/admin/products/edit?id=" + product.getId() + "&page=" + page;
    }
}


@PostMapping("/delete")
public String removeProduct(@Valid @ModelAttribute("product") Product product, BindingResult result,
                            RedirectAttributes redirectAttributes,
                            @RequestParam(value = "page", defaultValue = "0") int page) {

     if (!dao.existsById(product.getId())) {
         redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa!");
        return "redirect:/admin/products?page=" + page;
    }

    try {
        dao.delete(product);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa thành công!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm đang được sử dụng!");
    }

    return "redirect:/admin/products?page=" + page;
}


    @GetMapping("/edit")
    public String edit(@RequestParam("id") String id, @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {
        Optional<Product> optionalProduct = dao.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            model.addAttribute("imageSrc", "/images/" + product.getImage());
            model.addAttribute("product", product);

            int pageSize = 5; // Số lượng mục trên mỗi trang
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Product> productPage = dao.findAll(pageable);

            setProductPageInfo(model, productPage, page);
        }

        model.addAttribute("template", "Products.html");
        model.addAttribute("fragment", "content");
        return "admin/main-layout";
    }

    @GetMapping("/sort")
    public String sortProducts(Model model, @RequestParam("field") String field,
            @RequestParam(value = "direct", defaultValue = "ASC") String direct,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        String sortDirection = direct.equalsIgnoreCase("ASC") ? "DESC" : "ASC";
        int pageSize = 5; // Số lượng mục trên mỗi trang
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.fromString(direct), field));
        Page<Product> productPage = dao.findAll(pageable);

        setProductPageInfo(model, productPage, page);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("product", new Product());
        model.addAttribute("template", "Products.html");
        model.addAttribute("fragment", "content");

        return "admin/main-layout";
    }

 @GetMapping("/search")
public String searchProducts(Model model, @RequestParam("keyword") String keyword,
                             @RequestParam(value = "page", defaultValue = "0") int page) {
    int pageSize = 5; // Số lượng mục trên mỗi trang

    // Lưu trữ trang ban đầu
    int initialPage = page;

    Pageable pageable = PageRequest.of(page, pageSize);
    Page<Product> productPage = dao.findByKeyword(keyword, pageable);

    model.addAttribute("product", new Product());
    model.addAttribute("searchKeyword", keyword); // Thêm từ khóa tìm kiếm vào model

    if (productPage.isEmpty()) {
        // Không có kết quả tìm kiếm, trở về danh sách ban đầu
        page = initialPage;
        pageable = PageRequest.of(page, pageSize);
        productPage = dao.findByKeyword("", pageable); // Sử dụng từ khóa ban đầu để lấy danh sách ban đầu

        model.addAttribute("noResultsMessage", "Không tìm thấy kết quả nào cho từ khóa \"" + keyword + "\"");
    }

    setProductPageInfo(model, productPage, page);
    model.addAttribute("template", "Products.html");
    model.addAttribute("fragment", "content");

    return "admin/main-layout";
}


}