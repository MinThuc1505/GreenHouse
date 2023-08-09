package com.greenhouse.service;

public interface EmailService {
    void sendEmailForgotPassword(String to, String emailContent);
}

