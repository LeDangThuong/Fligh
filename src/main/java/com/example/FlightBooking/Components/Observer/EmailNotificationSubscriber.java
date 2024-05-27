package com.example.FlightBooking.Components.Observer;

import com.example.FlightBooking.Utils.EmailUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationSubscriber implements Subscriber {
    private final EmailUtils emailUtils;

    @Autowired
    public EmailNotificationSubscriber(EmailUtils emailUtils) {
        this.emailUtils = emailUtils;
    }

    @Override
    public void update(String context, String eventType) {
        try {
            switch (eventType) {
                case "REGISTER":
                    emailUtils.sendRegistrationEmail(context);
                    break;
                case "PASSWORD_RESET":
                    emailUtils.sendSetPasswordEmail(context);
                    break;
                case "BOOK_TICKET":
                    String[] details = context.split(";");
                    String email = details[0];
                    String ticketDetails = details[1];
                    emailUtils.sendTicketEmail(email, ticketDetails);
                    break;
                default:
                    break;
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
