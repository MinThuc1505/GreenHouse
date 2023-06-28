package com.greenhouse.controller.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.greenhouse.model.Account;
import com.greenhouse.model.Cart;
import com.greenhouse.model.Message;
import com.greenhouse.service.SessionService;

@Controller
@RequestMapping("/client/cart")
public class CartController {

    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private CartDAO cartDAO;

    @GetMapping("")
    public String getCart(Model model, @RequestParam("checkedIDs") Optional<String> checkedIDs) {
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
        var carts = cartDAO.findByAccountUsername(account.getUsername());
        model.addAttribute("carts", carts);


        model.addAttribute("message", Message.message);
        model.addAttribute("typeMessage", Message.type);
        Message.message = "";
        model.addAttribute("template", "cart.html");
        model.addAttribute("fragment", "content");
        return "client/main-layout";
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateCart(@RequestParam("id") Optional<Integer> id,
            @RequestParam("quantity") Optional<Integer> quantity) {
        Cart cart = cartDAO.findById(id.get()).get();
        cart.setQuantity(quantity.get());
        cartDAO.save(cart);
        Map<String, Object> result = new HashMap<>();
        result.put("quantity", cart.getQuantity());
        result.put("price", cart.getPrice());
        return ResponseEntity.ok(result);

    }

    @GetMapping("/delete")
    public String deleteCart(@RequestParam("checkedIDs") String checkedIDs) {
        String[] checked = checkedIDs.split(",");
        for (String string : checked) {
            cartDAO.deleteById(Integer.parseInt(string));
        }
        return "redirect:/client/cart";
    }

}
