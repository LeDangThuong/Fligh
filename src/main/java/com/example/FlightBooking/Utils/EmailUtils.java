package com.example.FlightBooking.Utils;

import com.example.FlightBooking.Models.Veritifications;
import com.example.FlightBooking.Repositories.UserRepository;
import com.example.FlightBooking.Repositories.VeritificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.imageio.ImageIO;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.transaction.Transactional;
import springfox.documentation.annotations.ApiIgnore;

@Component
@Hidden
public class EmailUtils {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private OtpUtils otpUtil;
    @Autowired
    private VeritificationRepository veritificationRepository;

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
        // Trước khi thêm OTP mới, xóa các OTP cũ của email này
        veritificationRepository.deleteByEmail(email);
        Long expirationTime = 360000L;
        Veritifications otpVerification = new Veritifications();
        otpVerification.setEmail(email);
        otpVerification.setCodeOTP(otp);
        otpVerification.setExpireTime(expirationTime);
        veritificationRepository.save(otpVerification);
    }
}
