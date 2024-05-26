package com.example.FlightBooking.Components.Decorator;

import com.example.FlightBooking.Models.Decorator.BaggageFees;

public class BaggageFeeDecorator extends BaseDecorator{
    private final BaggageFees baggageFee;
    private final double baggageWeight;

    public BaggageFeeDecorator(FlightComponent wrappee, BaggageFees baggageFee, double baggageWeight) {
        super(wrappee);
        this.baggageFee = baggageFee;
        this.baggageWeight = baggageWeight;
    }

    @Override
    public double calculateTotalPrice() {
        double totalPrice = super.calculateTotalPrice();
        if (baggageWeight > baggageFee.getWeightThreshold()) {
            totalPrice += (baggageWeight - baggageFee.getWeightThreshold()) * baggageFee.getFeePerKg();
        }
        return totalPrice;
    }
}
