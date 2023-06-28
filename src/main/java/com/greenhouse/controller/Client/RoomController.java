package com.greenhouse.controller.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.model.Account;
import com.greenhouse.service.SessionService;

@Controller
@RequestMapping("/client/room")
public class RoomController {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private AccountDAO accountDAO;

    @GetMapping("/living-room")
    public String getLivingRoom(Model model) {
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
        model.addAttribute("template", "living-room.html");
        model.addAttribute("fragment", "content");
        return "client/main-layout";
    }

    @GetMapping("/bed-room")
    public String getBedRoom(Model model) {
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
        model.addAttribute("template", "bed-room.html");
        model.addAttribute("fragment", "content");
        return "client/main-layout";
    }

    @GetMapping("/dining-room")
    public String getDiningRoom(Model model) {
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
        model.addAttribute("template", "dining-room.html");
        model.addAttribute("fragment", "content");
        return "client/main-layout";
    }

    @GetMapping("/work-room")
    public String getWorkRoom(Model model) {
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
        model.addAttribute("template", "work-room.html");
        model.addAttribute("fragment", "content");
        return "client/main-layout";
    }
}
