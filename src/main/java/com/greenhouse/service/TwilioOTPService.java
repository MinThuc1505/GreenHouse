package com.greenhouse.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenhouse.config.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class TwilioOTPService {

    @Autowired
    private TwilioConfig twilioConfig;

    public Map<String, String> sendOTP(String phoneNumber, String otp) {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());

        Map<String, String> result = new HashMap();
        PhoneNumber to = new PhoneNumber(phoneNumber);
        PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());

        String otpMessage = "Mã xác nhận từ GreenHouse gửi đến bạn là: " + otp;
        Message message = Message.creator(
                to,
                from,
                otpMessage)
                .create();

        result.put("phoneNumber", phoneNumber);
        result.put("otp", otp);
        return result;
    }

    public String generateOTP() {
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }
}
