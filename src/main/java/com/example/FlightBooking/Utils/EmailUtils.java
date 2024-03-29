package com.example.FlightBooking.Utils;

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

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.transaction.Transactional;

@Component
public class EmailUtil {

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
        String otp = otpUtil.generateOtp();
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

    @Transactional
    public void sendQRCodeToEmail(String email, BufferedImage qrImage) throws MessagingException, IOException {
        // Chuyển đổi BufferedImage thành byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", byteArrayOutputStream);
        byte[] qrImageData = byteArrayOutputStream.toByteArray();

        // Gửi email với attachment là hình ảnh QR code
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("QR Code");
        mimeMessageHelper.setText("Please find your QR Code attached below.");
        mimeMessageHelper.addAttachment("qrcode.png", new ByteArrayDataSource(qrImageData, "image/png"));
        javaMailSender.send(mimeMessage);
    }
    private void saveOTPInDatabase(String email, String otp) {
        // Trước khi thêm OTP mới, xóa các OTP cũ của email này
        veritificationRepository.deleteByEmail(email);
        Timestamp expirationTime = Timestamp.valueOf(LocalDateTime.now().plusMinutes(1));
        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setEmail(email);
        otpVerification.setOtp(otp);
        otpVerification.setExpirationTime(expirationTime);
        otpVerificationRepository.save(otpVerification);
    }
}
