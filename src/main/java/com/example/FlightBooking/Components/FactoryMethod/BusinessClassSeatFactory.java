package com.example.FlightBooking.Components.FactoryMethod;

import com.example.FlightBooking.Enum.SeatClass;
import com.example.FlightBooking.Models.Planes;
import com.example.FlightBooking.Models.Seats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessClassSeatFactory implements SeatFactory{
    private static final int NUM_ROWS = 6;
    private static final int SEATS_PER_ROW = 4;
    private static final String[] SEAT_LETTERS = {"G", "H", "I", "J", "K", "L"};

    @Override
    public Map<String, String> createSeats(Planes plane) {
        Map<String, String> seatStatuses = new HashMap<>();
        for (int row = 1; row <= NUM_ROWS; row++) {
            for (int seat = 1; seat <= SEATS_PER_ROW; seat++) {
                String seatNumber = SEAT_LETTERS[(row - 1) % SEAT_LETTERS.length] + seat;
                seatStatuses.put(seatNumber, "AVAILABLE");
            }
        }
        return seatStatuses;
    }
}
