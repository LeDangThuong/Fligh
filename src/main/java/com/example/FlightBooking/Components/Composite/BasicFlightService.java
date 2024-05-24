package com.example.FlightBooking.Components.Composite;

import java.util.Collections;
import java.util.List;

public class BasicFlightService implements FlightComponent{
    @Override
    public String getDescription() {
        return "Basic Flight Service";
    }

    @Override
    public double getCost() {
        return 0.0; // No additional cost for the basic service
    }

    @Override
    public List<String> getFlightDetails(Long flightId) {
        // Retrieve flight details from the database (mocked here)
        String details = "Flight ID: " + flightId +
                ", Departure: 21:50, Hanoi, Date: 25 Nov 2024" +
                ", Arrival: 01:50, Ho Chi Minh City, Date: 25 Nov 2024" +
                ", Duration: 2h 28m, Flight Number: VN-775, Airline: Vietnam Airlines";
        return Collections.singletonList(details);
    }

    @Override
    public List<String> getPriceAndBenefits(Long flightId) {
        // Retrieve price and benefits details from the database (mocked here)
        String priceAndBenefits = "Economy Price: $100, Business Price: $200, First Class Price: $300" +
                ", Baggage Free: 23Kg, Additional Baggage: $20 per 20Kg";
        return Collections.singletonList(priceAndBenefits);
    }

    @Override
    public List<String> getPromotions(Long flightId) {
        // Basic service doesn't include promotions
        return Collections.singletonList("No promotions available.");
    }
}
