package com.example.FlightBooking.DTOs.Request.Booking;

import lombok.Data;

import java.util.Set;

@Data
public class CombineBookingRequestDTO {
    private BookingRequestDTO bookingRequestDTO;
    private Set<String> seatNumber;
}
