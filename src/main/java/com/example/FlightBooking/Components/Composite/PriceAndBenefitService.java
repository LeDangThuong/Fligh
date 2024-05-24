package com.example.FlightBooking.Components.Composite;

import java.util.Collections;
import java.util.List;

public class PriceAndBenefitService implements FlightComponent {

    @Override
    public String getDescription() {
        return "Price and Benefit Service";
    }

    @Override
    public double getCost() {
        return 0.0; // No additional cost for this service
    }

    @Override
    public List<String> getFlightDetails(Long flightId) {
        return Collections.emptyList(); // This service does not handle flight details
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
        return Collections.emptyList(); // This service does not handle promotions
    }
}