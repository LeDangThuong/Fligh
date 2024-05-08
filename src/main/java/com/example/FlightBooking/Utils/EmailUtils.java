package com.example.FlightBooking.Utils;

import com.example.FlightBooking.Models.Verifications;
import com.example.FlightBooking.Repositories.UserRepository;
import com.example.FlightBooking.Repositories.VerificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

import java.time.temporal.ChronoUnit;
@Component
@Hidden
public class EmailUtils {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private OtpUtils otpUtil;
    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private UserRepository userRepository;
    @Transactional
    public void sendSetPasswordEmail (String email) throws MessagingException
    {
        Long otp = Long.valueOf(otpUtil.generateOtp());
        saveOTPInDatabase(email, otp);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Set Password");
        mimeMessageHelper.setText("""
            <div>
                Your OTP for password change is: %s
            </div>
            """.formatted(otp), true);
        javaMailSender.send(mimeMessage);
    }

    private void saveOTPInDatabase(String email, Long otp) {
        LocalDateTime expireTime = LocalDateTime.now().plus(5, ChronoUnit.MINUTES);
        Verifications otpVerification = new Verifications();
        otpVerification.setEmail(email);
        otpVerification.setCodeOTP(otp);
        otpVerification.setExpireTime(expireTime);
        verificationRepository.save(otpVerification);
    }
}
