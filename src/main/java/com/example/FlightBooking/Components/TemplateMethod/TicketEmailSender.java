package com.example.FlightBooking.Components.TemplateMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
public class TicketEmailSender extends AbstractEmailSender {
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    protected String getSubject() {
        return "Your Flight Ticket Details";
    }

    @Override
    protected String getBody(String ticketDetails) {
        String airline = "VietnamEline";
        String departure = "SGN";
        String arrival = "HAN";
        String passengerName = "Ho Chi Minh City";
        String flightNumber = "HG-9849";
        String gate = "A01";
        String seat = "B08";
        String boardingTime = "Monday, Auguest 28 2034";

        Context context = new Context();
        context.setVariable("airline", airline);
        context.setVariable("departure", departure);
        context.setVariable("arrival", arrival);
        context.setVariable("passengerName", passengerName);
        context.setVariable("flightNumber", flightNumber);
        context.setVariable("gate", gate);
        context.setVariable("seat", seat);
        context.setVariable("boardingTime", boardingTime);

        String htmlContent = templateEngine.process("ticketsEmail", context);

        return htmlContent;
    }
}