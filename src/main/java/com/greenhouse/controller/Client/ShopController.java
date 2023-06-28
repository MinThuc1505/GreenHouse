package com.greenhouse.controller.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.DAO.CartDAO;
import com.greenhouse.DAO.CategoryDAO;
import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.model.Account;
import com.greenhouse.model.Cart;
import com.greenhouse.model.Product;
import com.greenhouse.service.SessionService;

@Controller
@RequestMapping("/client/shop")
public class ShopController {

    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private CategoryDAO categoryDAO;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private CartDAO cartDAO;

    @GetMapping("")
    public String getShop(Model model, @RequestParam("category") Optional<String> categoryId) {
        if (categoryId.isPresent()) {
            var products = productDAO.findByCategory(categoryId.get());
            model.addAttribute("products", products);
        }else{
            var products = productDAO.findAll();
            model.addAttribute("products", products);
        }

        var categories = categoryDAO.findCategoryNotLike("Phòng");
        model.addAttribute("categories", categories);
        var rooms = categoryDAO.findCategoryLike("Phòng");
        model.addAttribute("rooms", rooms);

        // User Session - start
        Account account = sessionService.get("account");
        if (account != null) {
            try {
                int qty = accountDAO.findQuanityCartById(account.getId());
                model.addAttribute("qtyCart", qty);
            } catch (Exception e) {
                model.addAttribute("qtyCart", 0);
            }
            model.addAttribute("sessionUsername", account.getUsername());
        }
        // User Session - end
        model.addAttribute("template", "shop.html");
        model.addAttribute("fragment", "content");
        return "client/main-layout";
    }

    @PostMapping("/filter")
    public String postFilter(Model model, @RequestParam("price") int price, @RequestParam("category") String category) {
        // User Session - start
        Account account = sessionService.get("account");
        if (account != null) {
            try {
                int qty = accountDAO.findQuanityCartById(account.getId());
                model.addAttribute("qtyCart", qty);
            } catch (Exception e) {
                model.addAttribute("qtyCart", 0);
            }
            model.addAttribute("sessionUsername", account.getUsername());
        }
        // User Session - end
        if (price == 0) {
            var products = productDAO.findByCategory(category);
            model.addAttribute("products", products);
        } else {
            var products = productDAO.findByCategoryAndPrice(category, price);
            model.addAttribute("products", products);
        }

        model.addAttribute("categoryOld", category);
        model.addAttribute("priceOld", price);

        var categories = categoryDAO.findCategoryNotLike("Phòng");
        model.addAttribute("categories", categories);

        model.addAttribute("template", "shop.html");
        model.addAttribute("fragment", "content");
        return "client/main-layout";
    }

    @GetMapping("/filter")
    public String getFilter() {
        return "redirect:/client/shop";
    }

    @PostMapping("/add-cart")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestParam("id") String productId) {
        Account account = sessionService.get("account");
        if (account != null) {
            Optional<Product> product = productDAO.findById(productId);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sản phẩm không tồn tại");
            }
            Cart cart = cartDAO.findByProductIdAndAccountId(productId, account.getId());
            if (cart != null) {
                int qty = cart.getQuantity();
                cart.setQuantity(qty + 1);
                cartDAO.save(cart);
            } else {
                cart = new Cart();
                cart.setAccountId(account.getId());
                cart.setProductId(product.get().getId());
                cart.setPrice(product.get().getPrice());
                cart.setQuantity(1);
                cart.setStatus(false);
                cartDAO.save(cart);
            }
            String message = "Thêm sản phẩm thành công!";
            int qty = accountDAO.findQuanityCartById(account.getId());
            Map<String, Object> items = new HashMap<String, Object>();
            items.put("qty", qty);
            items.put("message", message);
            items.put("totalItems", qty);
            items.put("typeMessage", "success");
            return ResponseEntity.ok(items);
        }
        Map<String, Object> items = new HashMap<String, Object>();
        items.put("message", "Vui lòng đăng nhập trước khi mua hàng!");
        items.put("typeMessage", "error");
        return ResponseEntity.ok(items);
    }
}
