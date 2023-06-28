package com.greenhouse.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender mailSender;
    private ResourceLoader resourceLoader;

    public EmailServiceImpl(JavaMailSender mailSender, ResourceLoader resourceLoader) {
        this.mailSender = mailSender;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void sendEmailActiveAccount(String to, String subject, String idAccount) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Thiết lập thông tin người nhận, chủ đề và nội dung email
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(emailActiveAccount(idAccount), true);

            // Gửi email
            mailSender.send(message);
        } catch (MessagingException e) {
            // Xử lý ngoại lệ nếu gửi email thất bại
            e.printStackTrace();
        }
    }

    @Override
    public void sendEmailForgotPassword(String to, String subject, String idAccount) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Thiết lập thông tin người nhận, chủ đề và nội dung email
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(emailForgotPassword(idAccount), true);

            // Gửi email
            mailSender.send(message);
        } catch (MessagingException e) {
            // Xử lý ngoại lệ nếu gửi email thất bại
            e.printStackTrace();
        }
    }

    private String getEmailContentFromFile(String filePath) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + filePath);
            byte[] contentBytes = Files.readAllBytes(resource.getFile().toPath());
            return new String(contentBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Xử lý ngoại lệ nếu không đọc được nội dung email từ file
            e.printStackTrace();
            return "";
        }
    }

    private String emailActiveAccount(String activationLink) {
        return "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta http-equiv='X-UA-Compatible' content='IE=edge'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Email Confirmation</title><style>body {font-family: Arial, sans-serif;background-color: #f2f2f2;margin: 0;padding: 0;}.container{max-width: 800px;margin: 0 auto;padding: 20px;background-color: #fff;border-radius: 5px;box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);}h1{color: #333;text-align: center;}p{margin-bottom: 20px;text-align: center;}.btn{display: inline-block;padding: 10px 20px;background-color: #007bff;color: #fff;text-decoration: none;border-radius: 3px;}.btn:hover {background-color: #0056b3;}.company{color: #18cf00;}</style></head><body><div class='container'><h1>KÍCH HOẠT TÀI KHOẢN <span class='company'>GREEN HOUSE</span></h1><p>Cảm ơn bạn đã đăng ký tài khoản GreenHouse, hãy nhấn vào nút bên dưới để hoàn tất đăng ký.</p><p><a class='btn' href='"
                + activationLink + "'style='color: #fff;'>KÍCH HOẠT</a></p></div></body></html>";
    }

    private String emailForgotPassword(String activationLink) {
        return "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta http-equiv='X-UA-Compatible' content='IE=edge'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Email Confirmation</title><style>body {font-family: Arial, sans-serif;background-color: #f2f2f2;margin: 0;padding: 0;}.container {max-width: 800px;margin: 0 auto;padding: 20px;background-color: #fff;border-radius: 5px;box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);}.container h1 {color: #333;text-align: center;}.container p {margin-bottom: 20px;text-align: center;}.container .btn {display: inline-block;padding: 10px 20px;background-color: #007bff;color: #fff;text-decoration: none;border-radius: 3px;}.container .btn:hover {background-color: #0056b3;}.container .company {color: #18cf00;}</style></head><body><div class='container'><h1>YÊU CẦU ĐẶT LẠI MẬT KHẨU <span class='company'>GREEN HOUSE</span></h1><p>Hãy bấm vào nút bên dưới để tiếp tục đặt lại mật khẩu cho tài khoản của bạn.</p><p><a class='btn' href='"
                + activationLink + "' style='color: #fff;'>ĐẶT LẠI MẬT KHẨU</a></p></div></body></html>";
    }
}
