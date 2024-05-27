package com.example.FlightBooking.Services.UserService;

import com.example.FlightBooking.Components.Observer.EmailNotificationSubscriber;
import com.example.FlightBooking.Components.Observer.LoggingSubscriber;
import com.example.FlightBooking.Components.Observer.Publisher;
import com.example.FlightBooking.Models.Verifications;
import com.example.FlightBooking.Utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ObserverService {
    private final Publisher publisher;
    private final EmailUtils emailUtils;

    @Autowired
    public ObserverService(EmailNotificationSubscriber emailNotificationSubscriber, LoggingSubscriber loggingSubscriber, EmailUtils emailUtils) {
        this.publisher = new Publisher();
        this.emailUtils = emailUtils;
        publisher.subscribe(emailNotificationSubscriber);
        publisher.subscribe(loggingSubscriber);
    }

    public void registerUser(String email) {
        publisher.setMainState(email, "REGISTER");
    }

    public void requestPasswordReset(String email) {
        publisher.setMainState(email, "PASSWORD_RESET");
    }

    public boolean verifyOTP(String email, Long inputOTP) {
        Verifications verification = emailUtils.getVerification(email);
        return verification != null && verification.getCodeOTP().equals(inputOTP) && verification.getExpireTime().isAfter(LocalDateTime.now());
    }

    public void bookTicket(String email, String ticketDetails) {
        publisher.setMainState(email + ";" + ticketDetails, "BOOK_TICKET");
    }
}
