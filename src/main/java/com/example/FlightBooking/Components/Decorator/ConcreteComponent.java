package com.example.FlightBooking.Components.Decorator;

import com.example.FlightBooking.Models.Flights;

public class ConcreteComponent implements FlightComponent{
    private final Flights flight;
    private final int numberOfTickets;
    private final boolean isRoundTrip;

    public ConcreteComponent(Flights flight, int numberOfTickets, boolean isRoundTrip) {
        this.flight = flight;
        this.numberOfTickets = numberOfTickets;
        this.isRoundTrip = isRoundTrip;
    }

    @Override
    public double calculateTotalPrice() {
        double ticketPrice = flight.getEconomyPrice(); // Mặc định là Economy, bạn có thể thay đổi logic để nhận các loại vé khác
        int multiplier = isRoundTrip ? 2 : 1;
        return numberOfTickets * ticketPrice * multiplier;
    }
}
