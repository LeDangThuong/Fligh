package com.example.FlightBooking.Components.FactoryMethod;

import com.example.FlightBooking.Enum.SeatClass;
import com.example.FlightBooking.Models.Planes;
import com.example.FlightBooking.Models.Seats;

import java.util.List;
import java.util.Map;

public interface SeatFactory {
    Map<String, String> createSeats(Planes plane);
}