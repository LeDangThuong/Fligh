package com.example.FlightBooking.DTOs.Request.Booking;

import com.example.FlightBooking.DTOs.Request.Passenger.PassengerRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class BookingRequest {
    private Long flightId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private List<PassengerRequest> passengers;
}
