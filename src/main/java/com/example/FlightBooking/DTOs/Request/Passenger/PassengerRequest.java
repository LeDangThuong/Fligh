package com.example.FlightBooking.DTOs.Request.Passenger;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PassengerRequest {
    private String passengerType; // ADULT, CHILD
    private String name;
    private String passportNumber;
    private String seatClass; // ECONOMY, BUSINESS, FIRST
}
