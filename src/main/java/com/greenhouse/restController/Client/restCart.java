package com.greenhouse.restController.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.DAO.CartDAO;
import com.greenhouse.DAO.ProductDAO;
import com.greenhouse.model.Cart;
import com.greenhouse.model.Product;

@RestController
@RequestMapping(value = "/client/rest/cart")
public class restCart {

    @Autowired
    private CartDAO cartDAO;
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private AccountDAO accountDAO;

    @GetMapping
    private ResponseEntity<Map<String, Object>> getCart(@RequestParam String username) {
        Map<String, Object> responseData = new HashMap<>();
        String status = "";
        String message = "";

        var carts = cartDAO.getProductInCartByUsername(username);
        if (carts != null) {
            status = "success";
            message = "Lấy sản phẩm lên giỏ hàng thành công";

            // Sử dụng Gson để chuyển danh sách carts thành chuỗi JSON
            Gson gson = new Gson();
            String cartsJson = gson.toJson(carts);
            responseData.put("data", cartsJson);
        } else {
            status = "error";
            message = "Lỗi lấy sản phẩm lên giỏ hàng";
            responseData.put("data", null);
        }
        responseData.put("status", status);
        responseData.put("message", message);

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/add")
    private ResponseEntity<Map<String, Object>> addToCart(@RequestParam String productId,
            @RequestParam String username) {
        String status = "";
        String message = "";
        Map<String, Object> responseData = new HashMap<>();
        Cart cart = cartDAO.getCart(username, productId);
        Product product = productDAO.findById(Integer.parseInt(productId)).get();
        if (cart != null) {
            // update quantity
            int quantity = cart.getQuantity() + 1;
            cart.setQuantity(quantity);
            cart.setAmount(getTotalAmount(cart));
            cartDAO.save(cart);
            status = "success";
            message = "Sản phẩm đã có -> thêm số lượng";
        } else {
            // add to the cart table and set default values for amount & quantity
            cart = new Cart();
            cart.setUsername(username);
            cart.setProduct(product);
            cart.setQuantity(1);
            cart.setPrice(product.getPrice());
            cart.setAmount(getTotalAmount(cart));
            cart.setStatus(true);
            cartDAO.save(cart);
            status = "success";
            message = "Thêm giỏ hàng thành công";
        }

        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/addFormProductDetail")
    private ResponseEntity<Map<String, Object>> addToCartFormProductDetail(@RequestParam String productId, @RequestParam String quantity, 
            @RequestParam String username) {
        String status = "";
        String message = "";
        Map<String, Object> responseData = new HashMap<>();
        Cart cart = cartDAO.getCart(username, productId);
        Product product = productDAO.findById(Integer.parseInt(productId)).get();
        if (cart != null) {
            // update quantity
            int qty = cart.getQuantity() + Integer.parseInt(quantity);
            cart.setQuantity(qty);
            cart.setAmount(getTotalAmount(cart));
            cartDAO.save(cart);
            status = "success";
            message = "Sản phẩm đã có -> thêm số lượng";
        } else {
            // add to the cart table and set default values for amount & quantity
            cart = new Cart();
            cart.setUsername(username);
            cart.setProduct(product);
            cart.setQuantity(Integer.parseInt(quantity));
            cart.setPrice(product.getPrice());
            cart.setAmount(getTotalAmount(cart));
            cart.setStatus(true);
            cartDAO.save(cart);
            status = "success";
            message = "Thêm giỏ hàng thành công";
        }

        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/remove")
    private ResponseEntity<Map<String, Object>> removeCart(@RequestParam String cartId) {
        Map<String, Object> responseData = new HashMap<>();
        String status = "";
        String message = "";

        try {
            Cart cart = cartDAO.findById(Integer.parseInt(cartId)).get();
            cartDAO.delete(cart);
            status = "success";
            message = "Đã xóa thành công sản phẩm " + cart.getProduct().getName() + " ra khỏi giỏ hàng";
        } catch (Exception e) {
            status = "erro";
            message = "Lỗi xóa sản phẩm ra khỏi giỏ hàng";
        }

        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/update")
    private ResponseEntity<Map<String, Object>> updateCart(@RequestParam String cartId, @RequestParam String quantity) {
        String status = "";
        String message = "";
        Map<String, Object> responseData = new HashMap<>();

        Cart cart = new Cart();
        cart = cartDAO.findById(Integer.parseInt(cartId)).get();
        cart.setQuantity(Integer.parseInt(quantity));
        cart.setAmount(getTotalAmount(cart));
        cartDAO.save(cart);

        var totalAmount = getTotalAmount(cart);

        responseData.put("totalAmount", totalAmount);
        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/getOldQuantity")
    private ResponseEntity<Map<String, Object>> getOneCart(@RequestParam String cartId) {
        Map<String, Object> responseData = new HashMap<>();

        Cart cart = cartDAO.findById(Integer.parseInt(cartId)).get();

        responseData.put("oldQty", cart.getQuantity());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/getTotalQuantity")
    private ResponseEntity<Map<String, Object>> getQtyCart(@RequestParam("username") String username) {
        String status = "";
        String message = "";
        Map<String, Object> responseData = new HashMap<>();
        Integer qtyCart = 0;
        if (accountDAO.existsById(username)) {
            qtyCart = accountDAO.getQtyCartByUsername(username);
            if (qtyCart == null) {
                qtyCart = 0;
            }
            status = "success";
            message = "Lấy số lượng sản phẩm thành công: " + qtyCart;
        }
        responseData.put("status", status);
        responseData.put("message", message);
        responseData.put("qtyCart", qtyCart);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/checkout")
    private ResponseEntity<Map<String, Object>> getOrder(@RequestParam String checkedIDsStr) {
        String status = "";
        String message = "";
        Map<String, Object> responseData = new HashMap<>();

        String[] checked = checkedIDsStr.split(";");
        boolean checkInStock = false;
        List<String> nameProducts = new ArrayList<String>();
        for (String string : checked) {
            var temp = cartDAO.findById(Integer.parseInt(string));
            Product product = productDAO.findById(temp.get().getProduct().getId()).orElse(null);
            int newInStock = product.getQuantity() - temp.get().getQuantity();
            if (newInStock < 0) {
                checkInStock = true;
                String nameProduct = product.getName();
                int quantityStock = product.getQuantity();
                nameProducts.add(
                        "Sản phẩm [" + nameProduct + "] không còn đủ để đặt hàng ( Còn: " + quantityStock + " )");
            }
        }

        if (checkInStock) {
            status = "error";
            message = "Lỗi sản phẩm không đủ để đặt hàng";

            responseData.put("productIsNotEnough", nameProducts);
            responseData.put("status", status);
            responseData.put("message", message);
        } else {
            var carts = new ArrayList<>();
            for (String string : checked) {
                var cart = cartDAO.getProductInCartByCartId(string);
                carts.add(cart);
            }
            status = "success";
            message = "Lấy sản phẩm lên giỏ hàng thành công";

            responseData.put("data", carts);
            responseData.put("status", status);
            responseData.put("message", message);
        }
        return ResponseEntity.ok(responseData);
    }

    private Double getTotalAmount(Cart cart) {
        double totalPrice = 0;
        totalPrice = cart.getQuantity() * cart.getPrice();
        return totalPrice;
    }
}
