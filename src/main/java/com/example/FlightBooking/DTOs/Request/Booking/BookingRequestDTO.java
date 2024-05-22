package com.example.FlightBooking.DTOs.Request.Booking;

import com.example.FlightBooking.DTOs.Request.Passenger.PassengerDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class BookingRequestDTO {
    private Long flightId;
    private List<String> selectedSeats;
    private String bookerFullName;
    private String bookerEmail;
    private String bookerPersonalId;
    private Long userId;
    private List<PassengerDTO> passengers;
}
