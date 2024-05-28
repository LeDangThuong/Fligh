package com.example.FlightBooking.Components.TemplateMethod;

import org.springframework.stereotype.Component;

@Component
public class TicketEmailSender extends AbstractEmailSender {

    @Override
    protected String getSubject() {
        return "Your Flight Ticket Details";
    }

    @Override
    protected String getBody(String ticketDetails) {
        return """
            <html>
            <body>
                <div style="font-family: Arial, sans-serif; line-height: 1.6; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 600px; margin: auto;">
                    <h2 style="text-align: center; color: #4CAF50;">Your Flight Ticket Details</h2>
                    <p>Hello,</p>
                    <p>Thank you for booking with us. Here are your flight ticket details:</p>
                    <p>%s</p>
                    <p>Thank you!</p>
                    <hr style="border: none; border-top: 1px solid #eee;">
                    <div style="text-align: center; color: #999; font-size: 12px;">
                        <p>FlightBooking Team</p>
                        <p>Do not reply to this email. If you have any questions, contact us at support@flightbooking.com.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(ticketDetails);
    }
}