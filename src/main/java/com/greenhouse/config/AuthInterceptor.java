package com.greenhouse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import com.greenhouse.model.Account;
import com.greenhouse.service.SessionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    SessionService session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        Account user = (Account) session.get("account");
        String error = "";
        if (user == null) {
            System.out.println("=================" + user);
            error = "Please login!";
            response.sendRedirect("/client/error?error=" + error);
            return false;
        } else if (!user.isRole() && uri.startsWith("/admin")) {
            System.out.println("=================" + user);
            error = "You do not have access!";
            response.sendRedirect("/client/error?error=" + error);
            return false;
        }

        return true;
    }
}
