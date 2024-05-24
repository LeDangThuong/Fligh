package com.example.FlightBooking.Components.Composite;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PromotionServicxe implements FlightComponent{
    @Override
    public String getDescription() {
        return "Promotion Service";
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
        return Collections.emptyList(); // This service does not handle price and benefits
    }

    @Override
    public List<String> getPromotions(Long flightId) {
        // Retrieve promotions from the database (mocked here)
        return Arrays.asList("Promotion 1: 10% off", "Promotion 2: $20 discount");
    }
}
