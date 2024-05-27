package com.example.FlightBooking.Components.Observer;

import com.example.FlightBooking.Models.Decorator.Vouchers;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.UserRepository;
import com.example.FlightBooking.Services.DecoratorService.VoucherService;
import com.example.FlightBooking.Utils.EmailUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailNotificationSubscriber implements Subscriber {
    private final EmailUtils emailUtils;
    private final UserRepository userRepository;
    private final VoucherService voucherService;

    @Autowired
    public EmailNotificationSubscriber(EmailUtils emailUtils, UserRepository userRepository, VoucherService voucherService) {
        this.emailUtils = emailUtils;
        this.userRepository = userRepository;
        this.voucherService = voucherService;
    }

    @Override
    public void update(String context, String eventType) {
        if ("NEW_VOUCHER".equals(eventType)) {
            Long voucherId = Long.parseLong(context);
            Vouchers voucher = voucherService.getVoucherById(voucherId);
            List<Users> users = userRepository.findAll();
            for (Users user : users) {
                try {
                    emailUtils.sendVoucherEmail(
                            user.getEmail(),
                            voucher.getVoucherName(),
                            voucher.getCode(),
                            voucher.getDiscountAmount().toString(),
                            voucher.getCreatedAt().toString(),
                            voucher.getVoucherImageUrl()
                    );
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
