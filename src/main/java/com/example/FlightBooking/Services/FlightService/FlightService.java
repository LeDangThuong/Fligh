package com.example.FlightBooking.Services.FlightService;

import com.example.FlightBooking.Models.Flights;
import com.example.FlightBooking.Repositories.FlightRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Flights createNewFlight(Flights flight) {
        return flightRepository.save(flight);
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
