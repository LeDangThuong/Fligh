package com.example.FlightBooking.Services.FlightService;

import com.example.FlightBooking.Models.Flights;
import com.example.FlightBooking.Models.Seats;
import com.example.FlightBooking.Repositories.FlightRepository;
import com.example.FlightBooking.Repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    @Autowired
    private SeatRepository seatRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }


    public List<Flights> searchFlightOneWay(Long departureAirportId, Long arrivalAirportId, Timestamp departureDate) {
        return flightRepository.searchFlightOneWay(departureAirportId, arrivalAirportId, departureDate);
    }

    public List<Flights> searchFlightRoundTrip(Long departureAirportId, Long arrivalAirportId, Timestamp departureDate, Timestamp returnDate) {
        return flightRepository.searchFlightRoundTrip(departureAirportId, arrivalAirportId, departureDate, returnDate);
    }

    public List<Flights> searchFlightMulti(Long departureAirportId, Long arrivalAirportId, Timestamp departureStartDate, Timestamp departureEndDate) {
        return flightRepository.searchFlightMulti(departureAirportId, arrivalAirportId, departureStartDate, departureEndDate);
    }
 }
