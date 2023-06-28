package com.greenhouse.service;

public interface EmailService {
    void sendEmailActiveAccount(String to, String subject, String emailContent);
    void sendEmailForgotPassword(String to, String subject, String emailContent);
}

