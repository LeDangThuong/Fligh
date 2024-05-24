package com.example.FlightBooking.Components.Composite;

import java.util.ArrayList;
import java.util.List;

public class FlightComposite implements FlightComponent {
    private List<FlightComponent> flightComponents = new ArrayList<>();

    public void add(FlightComponent component) {
        flightComponents.add(component);
    }

    public void remove(FlightComponent component) {
        flightComponents.remove(component);
    }

    @Override
    public String getDescription() {
        StringBuilder description = new StringBuilder();
        for (FlightComponent component : flightComponents) {
            description.append(component.getDescription()).append(", ");
        }
        return description.toString();
    }

    @Override
    public double getCost() {
        double cost = 0.0;
        for (FlightComponent component : flightComponents) {
            cost += component.getCost();
        }
        return cost;
    }

    @Override
    public List<String> getFlightDetails(Long flightId) {
        List<String> details = new ArrayList<>();
        for (FlightComponent component : flightComponents) {
            details.addAll(component.getFlightDetails(flightId));
        }
        return details;
    }

    @Override
    public List<String> getPriceAndBenefits(Long flightId) {
        List<String> priceAndBenefits = new ArrayList<>();
        for (FlightComponent component : flightComponents) {
            priceAndBenefits.addAll(component.getPriceAndBenefits(flightId));
        }
        return priceAndBenefits;
    }

    @Override
    public List<String> getPromotions(Long flightId) {
        List<String> promotions = new ArrayList<>();
        for (FlightComponent component : flightComponents) {
            promotions.addAll(component.getPromotions(flightId));
        }
        return promotions;
    }
}
