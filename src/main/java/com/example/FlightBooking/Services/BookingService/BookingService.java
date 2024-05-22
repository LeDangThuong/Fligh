package com.example.FlightBooking.Services.BookingService;

import com.example.FlightBooking.Models.Booking;
import com.example.FlightBooking.Models.Flights;
import com.example.FlightBooking.Models.Passengers;
import com.example.FlightBooking.Repositories.BookingRepository;
import com.example.FlightBooking.Repositories.FlightRepository;
import com.example.FlightBooking.Repositories.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PassengerRepository passengerRepository;

}
