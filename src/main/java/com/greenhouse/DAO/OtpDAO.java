package com.greenhouse.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenhouse.model.OTP;

public interface OtpDAO extends JpaRepository<OTP, Integer>{
    @Query(value = "select * from OTP where Username = ?1 and Status = 0", nativeQuery = true)
    List<OTP> getOTPstatus0ByUsername(String username);

    @Query(value = "select * from OTP where Username = ?1 and OTPCode = ?2 and Status = 0", nativeQuery = true)
    OTP getOTP(String username, String otp);

}
