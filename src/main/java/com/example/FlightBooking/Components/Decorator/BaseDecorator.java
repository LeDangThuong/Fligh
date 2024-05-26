package com.example.FlightBooking.Components.Decorator;

public abstract class BaseDecorator implements FlightComponent {
    protected FlightComponent wrappee;

    public BaseDecorator(FlightComponent  wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public double calculateTotalPrice() {
        return wrappee.calculateTotalPrice();
    }
}
