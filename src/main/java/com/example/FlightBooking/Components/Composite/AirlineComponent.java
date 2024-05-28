package com.example.FlightBooking.Components.Composite;

public interface AirlineComponent {
    String getDetails();
    void add(AirlineComponent component);
    void remove(AirlineComponent component);
}
