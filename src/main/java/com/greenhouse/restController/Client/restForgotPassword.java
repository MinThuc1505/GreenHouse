package com.greenhouse.restController.Client;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenhouse.DAO.AccountDAO;
import com.greenhouse.DAO.OtpDAO;
import com.greenhouse.config.TwilioConfig;
import com.greenhouse.model.Account;
import com.greenhouse.model.OTP;
import com.greenhouse.service.EmailService;
import com.greenhouse.service.TokenService;
import com.greenhouse.service.TwilioOTPService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/client/rest/forgot-password")
public class restForgotPassword {

    @Autowired
    private EmailService emailService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private OtpDAO otpDAO;
    @Autowired
    private TwilioOTPService twilioService;
    @Autowired
    private TwilioConfig twilioConfig;

    @PostMapping("/validateUsername")
    public ResponseEntity<Map<String, Object>> validateUsername(
            @RequestBody Map<String, String> requestData) {
        Map<String, Object> responseData = new HashMap<>();

        Account acc = accountDAO.findById(requestData.get("username")).orElse(null);
        if (acc != null) {
            responseData.put("status", "success");
            responseData.put("message", "Tài khoản có tồn tại");
            responseData.put("username", acc.getUsername());
        } else {
            responseData.put("status", "error");
            responseData.put("message", "Tài khoản không tồn tại");
        }
        System.out.println(acc);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/sendmail")
    public ResponseEntity<Map<String, String>> sendResetPasswordEmail(
            @RequestBody Map<String, String> requestData) {
        Map<String, String> responseData = new HashMap<>();
        String status = "";
        String message = "";
        try {
            String username = requestData.get("username");
            // Gửi mail đổi mật khẩu tài khoản
            Account acc = accountDAO.findById(username).orElse(null);
            System.out.println("Send mail: " + acc);
            if (acc != null) {
                String activationToken = tokenService.generateTokenByUsername(acc.getUsername());
                String activationLink = generateActivationLink(request, activationToken);
                emailService.sendEmailForgotPassword(acc.getEmail(), activationLink);
                status = "success";
                message = "Đã gửi yêu cầu đặt lại mật khẩu đến email của bạn!";
            } else {
                status = "error";
                message = "Email này chưa đăng ký tài khoản!";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOTPActivationAccount(
            @RequestBody Map<String, String> requestData) {
        Map<String, String> responseData = new HashMap<>();

        String username = requestData.get("username");
        System.out.println(username + "------------------------");

        Account acc = accountDAO.findById(username).orElse(null);
        System.out.println("Send OTP: " + acc);
        if (acc != null) {
            if (acc.getPhone() != null) {
                List<OTP> otpList = otpDAO.getOTPstatus0ByUsername(username);
                if (otpList != null) {
                    for (OTP otp : otpList) {
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        Timestamp timestamp = Timestamp.valueOf(currentDateTime);
                        otp.setUpdateAt(timestamp);
                        otp.setStatus(2);
                        otpDAO.save(otp);
                    }
                }
                String phone = "+84" + acc.getPhone().substring(1);

                LocalDateTime currentDateTime = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(currentDateTime);

                LocalDateTime expirationDateTime = currentDateTime.plus(5, ChronoUnit.MINUTES);
                Timestamp timestampExpiration = Timestamp.valueOf(expirationDateTime);

                String otp = twilioService.generateOTP();

                OTP otpModel = new OTP();
                otpModel.setAccount(acc);
                otpModel.setOtpCode(otp);
                otpModel.setCreateAt(timestamp);
                otpModel.setExpirationTime(timestampExpiration);
                otpModel.setStatus(0);

                otpDAO.save(otpModel);

                responseData = twilioService.sendOTP(phone, otp);
                requestData.put("status", "success");
                responseData.put("message", "Đã gửi OTP đến số điện thoại của bạn");
            }else{
                requestData.put("status", "error");
                responseData.put("message", "Tài khoản này chưa đăng ký số điện thoại");
            }

        }

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/validateOTP")
    public ResponseEntity<Map<String, Object>> validateOTPActivationAccount(
            @RequestBody Map<String, String> requestData) {
        Map<String, Object> responseData = new HashMap<>();
        String status = "";
        String message = "";

        String username = requestData.get("username");
        String otp = requestData.get("otp");

        OTP otpModel = otpDAO.getOTP(username, otp);
        if (otpModel != null) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(currentDateTime);
            Date date = otpModel.getExpirationTime();
            Instant instant = date.toInstant();
            LocalDateTime expirationTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

            if (currentDateTime.isBefore(expirationTime)) {
                // OTP chưa hết hạn, thực hiện xác thực
                otpModel.setUpdateAt(timestamp);
                otpModel.setStatus(1);
                otpDAO.save(otpModel);

                status = "success";
                message = "Mã OTP hợp lệ, hãy chuyển đến trang đổi mật khẩu";
            } else {
                // OTP đã hết hạn
                otpModel.setUpdateAt(timestamp);
                otpModel.setStatus(2);
                otpDAO.save(otpModel);

                status = "error";
                message = "Mã OTP đã hết hiệu lực";
            }
        } else {
            // OTP không hợp lệ
            status = "error";
            message = "Mã OTP không hợp lệ";
        }
        responseData.put("status", status);
        responseData.put("message", message);
        return ResponseEntity.ok(responseData);
    }

    private String generateActivationLink(HttpServletRequest request, String activationToken) {
        return request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + request.getContextPath() + "/client/change-password?&token="
                + activationToken;
    }
}
