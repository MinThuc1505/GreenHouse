package com.greenhouse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterConfig implements WebMvcConfigurer {
    @Autowired
    AuthInterceptor auth;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(auth)
                .addPathPatterns("/admin/**", "/client/cart/**", "/client/payment/**", "/client/logout",
                        "/client/profile", "/client/order")
                .excludePathPatterns("/static/**");
    }
}
