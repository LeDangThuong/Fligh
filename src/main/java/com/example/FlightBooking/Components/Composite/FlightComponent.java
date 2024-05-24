package com.example.FlightBooking.Components.Composite;

import java.util.List;

public interface FlightComponent {
    String getDescription();
    double getCost();
    List<String> getFlightDetails(Long flightId);
    List<String> getPriceAndBenefits(Long flightId);
    List<String> getPromotions(Long flightId);
}
