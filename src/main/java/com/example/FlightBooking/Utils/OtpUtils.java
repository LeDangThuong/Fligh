package com.example.FlightBooking.Utils;

import com.example.FlightBooking.Repositories.VerificationRepository;
import com.example.FlightBooking.Repositories.VeritificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.PersistenceContext;
import springfox.documentation.annotations.ApiIgnore;

@Component
@Hidden
public class OtpUtils {


    @Autowired
    private VerificationRepository verificationRepository;
    private final Set<String> otpSet = new HashSet<>();

    public String generateOtp() {
        String otp;
        do {
            otp = generateRandomOtp();
        } while (!otpSet.add(otp)); // Kiểm tra xem mã OTP đã tồn tại hay chưa
        return otp;
    }

    private String generateRandomOtp() {
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000; // Tạo số ngẫu nhiên từ 100000 đến 999999
        return String.valueOf(randomNumber);
    }
    // Check và Xóa các OTP sau mỗi 10 giay
    @Scheduled(fixedRate = 10000) // Mỗi 10 giây
    public void deleteExpiredOTP() {
      verificationRepository.deleteByExpireTime();
    }
}

